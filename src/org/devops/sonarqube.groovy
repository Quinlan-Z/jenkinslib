package org.devops

//scan
def SonarScan(sonarServer,projectName,projectDesc,projectPath){
    
      //定义服务器列表
      def servers = ["test":"sonarqube-test","prod":"sonarqube-prod"]
      
      withSonarQubeEnv("${servers[sonarServer]}"){
      def scannerHome = "/usr/local/sonar-scanner-4.3.0.2102-linux/"
      //def sonarServer = "http://192.168.3.237:9000"
      def sonarDate = sh returnStdout: true, script: 'date +%Y%m%d%H%M'
      sonarDate = sonarDate - "\n"
      
      sh """
     ${scannerHome}/bin/sonar-scanner  -Dsonar.projectKey=${projectName}  \
     -Dsonar.projectName=${projectName}  \
     -Dsonar.projectVersion=s2onarDate \
     -Dsonar.ws.timeout=30 \
     -Dsonar.projectDescription=${projectDesc}  \
     -Dsonar.links.homepage=http://www.baidu.com \
     -Dsonar.sources=${projectPath} \
     -Dsonar.sourceEncoding=UTF-8 \
     -Dsonar.java.binaries=target/classes \
     -Dsonar.java.test.binaries=target/test-classes \
     -Dsonar.java.surefire.report=target/surefire-reports
     """
      }
//     def qg = waitForQualityGate()
//     if (qg.statue != 'ok') {
//     error "pipeline aborted due to quality gate failure: ${qg.status}"
     
// }
}
