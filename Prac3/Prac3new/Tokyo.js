
function refreshSA(){
    var refresh = 1000;
    mytime = setTimeout('display_SA()',refresh);
}

function refreshCity(){
    var refresh = 1000;
    mytime = setTimeout('calcTokyo()',refresh);
}


function display_SA(){
    var x = new Date();
    document.getElementById('time').innerHTML = "Time in SA: " + x;
    refreshSA();
}
 display_SA();

 function calcTokyo() {
    var city = 'Tokyo';
    var offset = 1;
    var date = new Date();
    var utc = date.getTime() + (date.getTimezoneOffset() * 60000);
    var newDate= new Date(utc + (3600000*offset));
    document.getElementById('Tokyo').innerHTML = "Time in Tokyo: " + newDate.getHours()+":"+newDate.getMinutes()+":"+newDate.getSeconds();
    refreshCity();
}

calcTokyo();