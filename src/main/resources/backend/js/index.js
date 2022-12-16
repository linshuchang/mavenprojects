/* 自定义trim */
function trim (str) {  //删除左右两端的空格,自定义的trim()方法
  return str == undefined ? "" : str.replace(/(^\s*)|(\s*$)/g, "")
}

//获取url地址上面的参数
function requestUrlParam(argname){
  var url = location.href
  var arrStr = url.substring(url.indexOf("?")+1).split("&")//add.html?id=1600406728518197249&...如果存在多个参数按&分割
  for(var i =0;i<arrStr.length;i++)//id=1600406728518197249&name=...
  {
      var loc = arrStr[i].indexOf(argname+"=")//查找传进来的id=
      if(loc!=-1){//id=1600406728518197249，将id=替换为""得到完整id
          return arrStr[i].replace(argname+"=","").replace("?","")
      }
  }
  return ""
}
