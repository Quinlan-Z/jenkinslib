package org.devops

def Ansible(hosts,func){
    sh "ansible ${hosts} -m ${func} -a \"uname -r\""



}