pipeline {
    agent any
	stages {
	    stage("1.11.2") {
	        steps {
	            sh './gradlew :1.11.2:setupCiWorkspace'
	            sh './gradlew :1.11.2:clean'
	            sh './gradlew :1.11.2:build'
	            archive '1.11.2/build/libs/*jar'
	        }
	    }
	    stage("1.12.2") {
	        steps {
	            sh './gradlew :1.12.2:setupCiWorkspace'
	            sh './gradlew :1.12.2:clean'
	            sh './gradlew :1.12.2:build'
	            archive '1.12.2/build/libs/*jar'
	        }
	    }
	}
}