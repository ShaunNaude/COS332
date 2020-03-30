
function refreshSA(){
    var refresh = 1000;
    mytime = setTimeout('display_SA()',refresh);
}

function refreshCity(){
    var refresh = 1000;
    mytime = setTimeout('calcLondon()',refresh);
}


function display_SA(){
    var x = new Date();
    document.getElementById('time').innerHTML = "Time in SA: " + x;
    refreshSA();
}
 display_SA();

 function calcLondon() {
    var city = 'London';
    var offset = 1;
    var date = new Date();
    var utc = date.getTime() + (date.getTimezoneOffset() * 60000);
    var newDate= new Date(utc + (3600000*offset));
    document.getElementById('London').innerHTML = "Time in London: " + newDate.getHours()+":"+newDate.getMinutes()+":"+newDate.getSeconds();
    refreshCity();
}

calcLondon();