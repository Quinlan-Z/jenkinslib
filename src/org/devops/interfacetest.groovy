package org.devops

//运行测试
def RunTest(){
  antHome = tool "ANT"
  sh "cd InterfaceTest && ${antHome}/bin/ant -f build.xml"
 }