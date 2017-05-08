<?php

use Illuminate\Http\Request;

/*
|--------------------------------------------------------------------------
| API Routes
|--------------------------------------------------------------------------
|
| Here is where you can register API routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| is assigned the "api" middleware group. Enjoy building your API!
|
*/

Route::middleware('auth:api')->get('/user', function (Request $request) {
    return $request->user();
});

Route::get('/test',function(){
	return "Testing is in progress....";
});

Route::post('users/test','UserController@test');
Route::post('users/{id}','UserController@getUser');
Route::post('users/reg/register','UserController@register');
Route::post('users/reg/verify','UserController@verifyuser');
Route::post('users/login/{requestno}','UserController@login');
Route::post('users/email/send','UserController@sendemail');
Route::post('users/jwt/authenticate','UserController@getAuthenticatedUser');
Route::post('users/message/send','MessageController@SendMessage');
Route::post('users/message/{id}/{mid}','MessageController@GetMessage');
