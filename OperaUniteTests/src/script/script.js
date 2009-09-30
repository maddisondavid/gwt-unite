function testHandler(e)
{
    var req = e.connection.request;
    var res = e.connection.response;


    res.write('Test Complete');
    res.close();
}

opera.io.webserver.addEventListener('_index',testHandler, false);


