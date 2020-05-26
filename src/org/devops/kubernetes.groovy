package  org.devops


//封装HTTP请求
def HttpReq(reqType,reqUrl,reqBody){
    //1)  Gitlab Api地址
    def gitServer = "https://192.168.3.226:6443/apis/apps/v1"
    //2)  gitlab上创建一个凭据，存在给jenkins上,凭据名字(gitlab-token),赋值给gitlabToken变量
    withCredentials([string(credentialsId: 'kubernetes-test-token', variable: 'kubetest')]) {
      //  发送HTTP请求-->请求类型、等等信息-->请求的时候加到Header里面
      result = httpRequest customHeaders: [[maskValue: true, name: 'Authorization', value: "Bearer ${kubetest}"],[maskValue: false, name: 'Content-Type', value: 'application/yaml'],[maskValue: false, name: 'Accept', value: 'application/yaml']],
                httpMode: reqType,
                contentType: "TEXT_HTML",
                consoleLogResponseBody: true,
                ignoreSslErrors: true,
                requestBody: reqBody,
                url: "${gitServer}/${reqUrl}"
                //quiet: true
    }
    return result
}


//获取Deployment
def GetDeployment(nameSpace,deployName){
    apiUrl = "namespaces/${nameSpace}/deployments/${deployName}"
    response = HttpReq('GET',apiUrl,'')
    
    // def datas = readYaml text: """${response.content}"""
    // println(datas)
    return response
    
}

//更新Deployment
def UpdateDeployment(nameSpace,deployName,deplyBody){
    apiUrl = "namespaces/${nameSpace}/deployments/${deployName}"
    response = HttpReq('PUT',apiUrl,deplyBody)
    println(response)
}