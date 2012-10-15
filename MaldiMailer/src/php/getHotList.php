<?php

//for is668

$request =  'http://www.flickr.com/services/rest/?method=flickr.tags.getHotList&format=rest&period=day&count=10&api_key=your_api_key';

// send request to the service using curl - http://us2.php.net/curl
$ch = curl_init();    // initialize curl handle
curl_setopt($ch, CURLOPT_URL,$request); // set url  - must match above
curl_setopt($ch, CURLOPT_FAILONERROR, 1);
curl_setopt($ch, CURLOPT_FOLLOWLOCATION, 1);// allow redirects
curl_setopt($ch, CURLOPT_RETURNTRANSFER,1); // return into a variable
curl_setopt($ch, CURLOPT_TIMEOUT, 3); // times out after 4s
curl_setopt($ch, CURLOPT_GET, 1); // set GET method
$xml = curl_exec($ch); // run the whole process and put the response from the service into the $xml variable
curl_close($ch);

//send the http header for xml so the bowser know what it is - http://us3.php.net/header
header("Content-type: text/xml");
echo($xml);
?>
