
function refreshSA(){
    var refresh = 1000;
    mytime = setTimeout('display_SA()',refresh);
}

function refreshCity(){
    var refresh = 1000;
    mytime = setTimeout('calcNewYork()',refresh);
}


function display_SA(){
    var x = new Date();
    document.getElementById('time').innerHTML = "Time in SA: " + x;
    refreshSA();
}
 display_SA();

 function calcNewYork() {
    var city = 'NewYork';
    var offset = -4;
    var date = new Date();
    var utc = date.getTime() + (date.getTimezoneOffset() * 60000);
    var newDate= new Date(utc + (3600000*offset));
    document.getElementById('NewYork').innerHTML = "Time in NewYork: " + newDate.getHours()+":"+newDate.getMinutes()+":"+newDate.getSeconds();
    refreshCity();
}

calcNewYork();