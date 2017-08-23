/**
 * Created by lxy on 2017/3/17.
 */
// 设置Cookie
function addCookie(name, value, iDay){
    // 有过期时间
    if(iDay){
        var oDate=new Date();
        oDate.setDate(oDate.getDate()+iDay);
        document.cookie=name+'='+value+'; path=/; expires='+oDate.toUTCString();
        // 没有过期时间
    }else{
        document.cookie=name+'='+value+'; path=/';
    }
}
// 获取Cookie
function getCookie(name){
    var arr=document.cookie.split('; ');

    for(var i=0; i<arr.length; i++){
        var arr2=arr[i].split('=');
        if(arr2[0]==name){
            return arr2[1];
        }
    }
    return '';
}
// 删除
function removeCookie(name){
    addCookie(name, '123', -1);
}