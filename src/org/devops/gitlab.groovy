package  org.devops
 
//(1)封装HTTP请求
def HttpReq(reqType,reqUrl,reqBody){
    //1)  Gitlab Api地址
    def gitServer = "http://192.168.3.228/api/v4"
    //2)  gitlab上创建一个凭据，存在给jenkins上,凭据名字(gitlab-token),赋值给gitlabToken变量
    withCredentials([string(credentialsId: 'gitlab-token', variable: 'gitlabToken')]) {
      //  发送HTTP请求-->请求类型、等等信息-->请求的时候加到Header里面
      result = httpRequest customHeaders: [[maskValue: true, name: 'PRIVATE-TOKEN', value: "${gitlabToken}"]],
                httpMode: reqType,
                contentType: "APPLICATION_JSON",
                consoleLogResponseBody: true,
                ignoreSslErrors: true,
                requestBody: reqBody,
                url: "${gitServer}/${reqUrl}"
                //quiet: true
    }
    return result
}

//更新文件内容
def UpdateRepoFile(projectId,filePath,fileContent){
    apiUrl = "projects/${projectId}/repository/files/${filePath}"
    reqBody = """{"branch":"master","encoding":"base64", "content":"${fileContent}","commit_message":"update new file"}"""
    response = HttpReq('PUT',apiUrl,reqBody)
    println(response)
}
//获取文件内容
def GetRepoFile(projectId,filePath){
    apiUrl = "projects/${projectId}/repository/files/${filePath}/raw?ref=master"
    response = HttpReq('GET',apiUrl,'')
    return response.content
}
//创建仓库文件
// def CreateRepoFile(projectId,filePath,fileContent){
//     apiUrl = "projects/${projectId}/repository/files/${filePath}"
//     reqBody = """{"branch": "master", content":"${fileContent}","commit_message":"create new file"}"""
//     response = HttpReq('POST',apiUrl,reqBody)
//     println(response)
// }

def CreateRepoFile(projectId,filePath,fileContent){
    apiUrl = "projects/${projectId}/repository/files/${filePath}"
    reqBody = """{"branch": "master","encoding":"base64", "content":"${fileContent}","commit_message":"create new file"}"""
    response = HttpReq('POST',apiUrl,reqBody)
    println(response)
}


 
//更改提交状态
 def ChangeCommitStatus(projectId,commitSha,status){
    commitApi = "projects/${projectId}/statuses/${commitSha}?state=${status}"
    response = HttpReq('POST',commitApi,'')
    println(response)
    return response
}

//获取项目ID
def GetProjectID(repoName='',projectName){
    projectApi = "projects?search=${projectName}"
    response = HttpReq('GET',projectApi,'')
    def result = readJSON text: """${response.content}"""
    
    for  (repo in result){
            if (repo['path'] == "${projectName}"){
                repoId = repo['id']
                println(repoId)
    }
}
    return repoId
}

//删除分支
def DeleteBranch(projectId,branchName){
    apiUrl = "/projects/${projectId}/repository/branches/${branchName}"
    response = HttpRqe("DELETE",apiurl,'').content
    println(response)
    
}

//创建分支
def CreateBranch(projectId,refBranch,newBranch){
    try {
        branchApi = "projects/${projectId}/repository/branches?branch=${newBranch}&ref=${refBranch}"
        response = HttpRqe("DELETE",branchApi,'').content
        branchInfo = readJSON text: """${response}"""    
    } catch(e){
        println(e)
    }
}

//创建合并请求
def CreateMr(projectId,sourceBranch,targetBranch,title,assigneeUser=""){
    try {
        def mrUrl = "projects/${projectId}/merge_requests"
        def reqBody = """{"source_branch":"${sourceBranch}", "target_branch":"${targetBranch}","title":"${title}", "assignee_id":"${assigneeUser}"}"""
        response = HttpRqe("POST",mrUrl,reqBody).content
    } catch(e){
        println(e)
    }
}
