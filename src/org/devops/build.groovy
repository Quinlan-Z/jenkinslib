package org.devops

//构建类型

def Build(buildType,buildShell){
    def buildTools = ["mvn":"m2","ant":"ANT","gradle":"gradle"]

    buildHome = tool buildTools[buildType]
    println("当前选择的构建类型 ${buildType}")
    if ("${buildType}" == "ant"){
        sh "${buildHome}/bin/${buildType} -v"
    }
    else {
        sh "${buildHome}/bin/${buildType} ${buildShell}"
}
    


}