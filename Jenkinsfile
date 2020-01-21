pipeline {
	triggers {
		// poll repo every 5 minute for changes
		pollSCM('*/5 * * * *')
	}
	agent {
		// Run on a build agent where we have the Android SDK installed
		label 'master'
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
		stage('Deploy') {
			when {
				// Only execute this stage when building from the `internal`, `alpha` or `beta` branch
				anyOf {
				    branch 'internal'
				    branch 'alpha'
				    branch 'beta'
				}
			}
			environment {
				// Assuming a file credential has been added to Jenkins, with the ID 'my-app-signing-keystore',
				// this will export an environment variable during the build, pointing to the absolute path of
				// the stored Android keystore file.  When the build ends, the temporarily file will be removed.
				SIGNING_KEYSTORE = credentials('qrscanner-signing-keystore')

				//Password of the keystore
				SIGNING_KEYSTORE_PASSWORD = credentials('qrscanner-signing-keystore-password')

                //Keystore alias
                SIGNING_KEYSTORE_ALIAS = credentials('qrscanner-signing-keystore-alias')

				// Similarly, the value of this variable will be a password stored by the Credentials Plugin
				SIGNING_KEY_PASSWORD = credentials('qrscanner-signing-password')
			}
			steps {
			    withCredentials([file(credentialsId: 'qrscanner-signing-keystore', variable: 'KEYFILE')]) {
                     sh "cp \$KEYFILE app/keystore.jks"
                }

				// Build the app in release mode, and sign the AAB using the environment variables
				sh 'bash ./gradlew app:bundleRelease'

				// Archive the AABs so that they can be downloaded from Jenkins
				archiveArtifacts '**/*.aab'

				// Upload the AAB to Google Play
                androidAabUpload googleCredentialsId: 'Google Play', applicationId: 'nl.invissvenska.qrscanner', aabFilesPattern: '**/bundle/release/app-release.aab', trackName: env.BRANCH_NAME,
                    recentChangeList: [
                        [language: 'en-US', text: "Please test the changes from Jenkins build ${env.BUILD_NUMBER}."]
                    ]
			}
			post {
				success {
					// Notify if the upload succeeded
					mail to: 'sven.vd.tweel@gmail.com', subject: 'New build available in ' + env.BRANCH_NAME + '!', body: 'Check it out!'
				}
			}
		}
		stage('Cleanup Credential') {
            when {
                // Only execute this stage when building from the `internal`, `alpha` or `beta` branch
                anyOf {
                    branch 'internal'
                    branch 'alpha'
                    branch 'beta'
                }
            }
            sh "rm app/keystore.jks"
        }
	}
	post {
		failure {
			// Notify developer team of the failure
			mail to: 'sven.vd.tweel@gmail.com', subject: 'QR Scanner Android App Failure', body: "Build ${env.BUILD_NUMBER} failed; ${env.BUILD_URL}"
		}
	}
}