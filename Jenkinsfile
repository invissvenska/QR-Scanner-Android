pipeline {
	triggers {
		// poll repo every 5 minute for changes
		pollSCM('*/5 * * * *')
	}
	agent {
		// Run on a build agent where we have the Android SDK installed
		label 'master'
	}
	parameters {
		choice(choices: ['none', 'internal', 'alpha', 'beta'], description: 'Deploy app to selected track', name: 'DEPLOY_TRACK')
	}
	environment {
		ANDROID_SDK_ROOT = "/home/sven"
	}
	options {
		// Only have one build per branch
		disableConcurrentBuilds()
		// Stop the build early in case of compile or test failures
		skipStagesAfterUnstable()
		buildDiscarder(logRotator(numToKeepStr: '5', artifactNumToKeepStr: '5'))
	}
	stages {
		stage('Compile') {
			when {
				// Only execute this stage when building from the `develop` or `feature/*` branch
				anyOf {
					branch 'develop'
					branch 'feature/*'
				}
			}
			steps {
				// Compile the app and its dependencies
				sh 'bash ./gradlew compileDebugSources'
			}
		}

		stage('Tests') {
			parallel {
				stage('Unit test') {
					when {
						// Only execute this stage when building from the `develop` or `feature/*` branch
						anyOf {
							branch 'develop'
							branch 'feature/*'
						}
					}
					steps {
						// Compile and run the unit tests for the app and its dependencies
						sh 'bash ./gradlew testDebugUnitTest testDebugUnitTest'

						// Analyse the test results and update the build result as appropriate
						junit '**/TEST-*.xml'
					}
				}
				stage('Static analysis') {
					when {
						// Only execute this stage when building from the `develop` or `feature/*` branch
						anyOf {
							branch 'develop'
							branch 'feature/*'
						}
					}
					steps {
						// Run Lint and analyse the results
						sh 'bash ./gradlew lintDebug'
						androidLint pattern: '**/lint-results-*.xml'
					}
				}
			}
		}
		stage('Build APK') {
			when {
				// Only execute this stage when building from the `develop` or `feature/*` branch
				anyOf {
					branch 'develop'
					branch 'feature/*'
				}
			}
			steps {
				// Finish building and packaging the APK
				sh 'bash ./gradlew assembleDebug'

				// Archive the APKs so that they can be downloaded from Jenkins
				archiveArtifacts '**/*.apk'
			}
		}
		stage('Deploy') {
			when {
				// Only execute this stage when selected DEPLOY_TRACK is `internal`, `alpha` or `beta`
				expression {
					return params.DEPLOY_TRACK != 'none'
				}
			}
			environment {
				//Password of the keystore
				SIGNING_KEYSTORE_PASSWORD = credentials('qrscanner-signing-keystore-password')

				//Keystore alias
				SIGNING_KEYSTORE_ALIAS = credentials('qrscanner-signing-keystore-alias')

				// Similarly, the value of this variable will be a password stored by the Credentials Plugin
				SIGNING_KEY_PASSWORD = credentials('qrscanner-signing-password')
			}
			steps {
				//prepare certificate
				withCredentials([file(credentialsId: 'qrscanner-signing-keystore', variable: 'KEYFILE')]) {
					sh "cp \$KEYFILE app/qrscanner-keystore.jks"
				}

				// Build the app in release mode, and sign the AAB using the environment variables
				sh 'bash ./gradlew app:bundleRelease'

				// Archive the AABs so that they can be downloaded from Jenkins
				archiveArtifacts '**/*.aab'
				archiveArtifacts '**/mapping.txt'

				// Upload the AAB to Google Play
				androidApkUpload googleCredentialsId: 'Google Play',
					filesPattern: '**/bundle/release/app-release.aab',
					trackName: params.DEPLOY_TRACK,
					deobfuscationFilesPattern: '**/build/outputs/**/mapping.txt',
					recentChangeList: [
						[language: 'en-US', text: "Please test the changes from Jenkins build ${env.BUILD_NUMBER}."]
					]
			}
			post {
				success {
					// Notify if the upload succeeded
					mail to: 'sven.vd.tweel@gmail.com', subject: 'New build available in ' + params.DEPLOY_TRACK + '!', body: 'Check it out!'
				}
			}
		}
		stage('Cleanup Credential') {
			when {
				// Only execute this stage when selected DEPLOY_TRACK is `internal`, `alpha` or `beta`
				expression {
					return params.DEPLOY_TRACK != 'none'
				}
			}
			steps {
				sh "rm app/qrscanner-keystore.jks"
			}
		}
	}
	post {
		failure {
			// Notify developer team of the failure
			mail to: 'sven.vd.tweel@gmail.com', subject: 'QR Scanner Android App Failure', body: "Build ${env.BUILD_NUMBER} failed; ${env.BUILD_URL}"
		}
	}
}