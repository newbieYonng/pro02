function delFruit(fid){
    if(confirm('是否确认删除?')){
        window.location.href='del.do?fid='+fid;
    }
}

/*错误写法
function page(pageNo){
    window.location.href='index?pageNo='+pageNo;
正确写法
Function()
{
    window.page = Function(pageNo)
    {
        window.location.href = 'index?pageNo=' + pageNo;
    }
}
}*/
page=function (pageNo) {
    window.location.href='index?pageNo='+pageNo;
}
