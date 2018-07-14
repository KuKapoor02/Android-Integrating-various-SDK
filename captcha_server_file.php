
<?php

date_default_timezone_set("Asia/kolkata");
if(isset($_POST['token']) )
	{
		  // Downloaded from https://github.com/KuKapoor02
    // Visit https://www.ourcoaching.com/
		
	$secret = 'YOUR_APP_SECRET_WILL_COME_HERE';
	$verifyResponse = file_get_contents('https://www.google.com/recaptcha/api/siteverify?secret='.$secret.'&response='.$_POST['token']);
	$responseData = json_decode($verifyResponse);

    if($responseData->success)
	{
	echo "success";
	
	}else 
	echo "failure";

	}else echo "error, no input received";

?>