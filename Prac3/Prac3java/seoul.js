
function refreshSA(){
    var refresh = 1000;
    mytime = setTimeout('display_SA()',refresh);
}

function refreshCity(){
    var refresh = 1000;
    mytime = setTimeout('calcSeoul()',refresh);
}


function display_SA(){
    var x = new Date();
    document.getElementById('time').innerHTML = "Time in SA: " + x;
    refreshSA();
}
 display_SA();

 function calcSeoul() {
    var city = 'Seoul';
    var offset = 9;
    var date = new Date();
    var utc = date.getTime() + (date.getTimezoneOffset() * 60000);
    var newDate= new Date(utc + (3600000*offset));
    document.getElementById('Seoul').innerHTML = "Time in Seoul: " + newDate.getHours()+":"+newDate.getMinutes()+":"+newDate.getSeconds();
    refreshCity();
}

calcSeoul();