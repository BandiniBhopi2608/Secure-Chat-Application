<?php
$mysql_host = '127.0.0.1';
$mysql_user = 'root';
$mysql_password = 'bandbrin';
$mysql_db = 'SecureChat';

@mysqli_connect($mysql_host, $mysql_user, $mysql_password, $mysql_db) OR die('Connection Failed !!!!!');

echo 'Connection successful!!!!';
?>

