package org.devops
//封装HTTP

def HttpReq(reqType,reqUrl,reqBody){
    //1)  Gitlab Api地址
    def sonarServer = "http://192.168.3.237:9000/api"
    result = httpRequest authentication: 'sonar-admin-user',
                httpMode: reqType,
                contentType: "APPLICATION_JSON",
                consoleLogResponseBody: true,
                ignoreSslErrors: true,
                requestBody: reqBody,
                url: "${sonarServer}/${reqUrl}"
                //quiet: true
    return result
}

//获取Sonar状态
def GetProjectStatus(projectName){
    apiUrl = "project_branches/list?project=${projectName}"
    response = HttpReq("GET",apiUrl,'')
    response = readJSON text: """${response.content}"""
    result = response["branches"][0]["status"]["qualityGateStatus"]
    // println(response)
    return result

}

//搜索Sonar项目
def SearchProject(projectName){
    apiUrl = "projects/search?projects=${projectName}"
    response = HttpReq("GET",apiUrl,'')
    response = readJSON text: """${response.content}"""
    result = response["paging"]["total"]
    
    if(result.toString() == "0"){
        return "false"
    }else {
        return "true"
    }
}

//创建项目    
def CreateProject(projectName){
    apiUrl = "projects/create?name=${projectName}&project=${projectName}"
    response = HttpReq("POST",apiUrl,'')
    println(response)
}
    
//配置项目质量规则
def ConfigQualityProfiles(projectName,lang,qpname){
    apiUrl = "qualityprofiles/add_project?language=${lang}&project=${projectName}&qualityProfile=${qpname}"
    response = HttpReq("POST",apiUrl,'')
    println(response)
}

//获取质量阈id
def GetQualtyGateId(gateName){
    apiUrl = "qualitygates/show?name=${gateName}"
    response = HttpReq("POST",apiUrl,'')
    response = readJSON text: """${response.content}"""
    result = response["id"]
    return result
    
}

//配置项目质量阈
def ConfigQualityGates(projectName,gateName){
    gateId = GetQualtyGateId(gateName)
    apiUrl = "qualitygates/select?gateId=${gateId}&projectKey=${projectName}"
    response = HttpReq("POST",apiUrl,'')
    println(response)
}
