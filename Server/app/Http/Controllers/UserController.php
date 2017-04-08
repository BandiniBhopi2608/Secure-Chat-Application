<?php

namespace App\Http\Controllers;

use PHPMailer;
use App\Models\User;
use App\Models\Challenge;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Validator;

class UserController extends Controller
{
    public function test(){
		return "Success";
	}
	
	public function getUser($id) {		
		$user = User::find($id);
		if(empty($user)) {
			$error = ['error' => 'User Not Found', 'code' => '1'];
			return response()->json($error);
		}
		else {
			return response()->json($user);
		}
	}
	
	/*Reference: http://www.devnetwork.net/viewtopic.php?f=28&t=38810*/
	public function login(Request $request, $requestNo) {
		$input = $request->all();
		if($requestNo == 1) {
			$validationRules = ['PhoneNumber' => 'required|numeric|unique:User'];
			$errorMessages = [
				'PhoneNumber.required' => ' The Phone Number field is required.',
				'PhoneNumber.numeric' => ' The Phone Number should contain only numbers.',
				'PhoneNumber.unique' => ' The Phone Number should be unique.',
			];
			$validator = Validator::make($input, $validationRules, $errorMessages);
			if ($validator->fails()) {			
				return response()->json($validator->errors());
			}
			else {
				$user = User::where('PhoneNumber',$input['PhoneNumber']).get();
				if(empty($user)) {
					$error = ['error' => 'PhoneNumber Not Found', 'code' => '1'];
					return response()->json($error);
				}
				else {
					//In second phase consider timestamp
					$challenge = Challenge::firstOrNew(['PhoneNumber' => $input['PhoneNumber']]);
					$challenge->Challenge = bin2hex(random_bytes(32));
					$challenge->save();					
					$response = ['Salt' => $user->Salt
								,'Challenge' => $challenge->Challenge];
					return response()->json($response);
				}
			}
		}
		elseif($requestNo == 2) {
			$validationRules = ['PhoneNumber' => 'required|numeric|unique:User',
								'Tag' => 'required',
								'Challenge' => 'required'];
			$errorMessages = [
				'PhoneNumber.required' => ' The Phone Number field is required.',
				'PhoneNumber.numeric' => ' The Phone Number should contain only numbers.',
				'PhoneNumber.unique' => ' The Phone Number should be unique.',
				'Tag.required' => ' Tag is required.',
				'Challenge.required' => ' Challenge is required.'
			];
			$validator = Validator::make($input, $validationRules, $errorMessages);
			if ($validator->fails()) {			
				return response()->json($validator->errors());
			}
			else {
				$user = User::where('PhoneNumber',$input['PhoneNumber']).get();
				$challenge = Challenge::where(['Challenge' => $input['Challenge'], 
											   'PhoneNumber' => $input['PhoneNumber']])
									    .get();
										
				if(empty($user)) {
					$error = ['error' => 'PhoneNumber Not Found', 'code' => '1'];
					return response()->json($error);
				}
				elseif(empty($challenge)) {
					$error = ['error' => 'Challenge Not Found', 'code' => '1'];
					return response()->json($error);
				}
				else {
					$tag = $input['Tag'];
					$mytag = hash_hmac('sha512', $user->EncryptedPassword, $challenge->Challenge);
					if(strcmp($tag, $mytag) === 0) {
						return response()->json($user);						
					}
					else {
						$error = ['error' => 'LoginFail', 'code' => '0'];
						return response()->json($errors);
					}
				}
			}
		}
	}
	
	public function register(Request $request)	{
		
		$input = $request->all();
		
		$validationRules = [
			'UserName' => 'required|max:50',
			'Password' => 'required|max:80',
			'Country' => 'required|numeric',
			'PhoneNumber' => 'required|numeric|unique:User',
			'EmailID' => 'required|email|unique:User'
		];
		
		$errorMessages = [
			'UserName.required' => ' The User Name field is required.',
			'UserName.max' => ' The User Name may not be greater than 50 characters.',
			'Password.required' => ' The Password field is required.',
			'Password.max' => ' The User Name may not be greater than 80 characters.',
			'Country.required' => ' The Country field is required.',
			'Country.numeric' => ' The Country Code should be numeric.',
			'PhoneNumber.required' => ' The Phone Number field is required.',
			'PhoneNumber.numeric' => ' The Phone Number should contain only numbers.',
			'PhoneNumber.unique' => ' The Phone Number should be unique.',
			'EmailID.required' => ' The EmailID field is required.',
			'EmailID.unique' => ' The EmailID should be unique.',
		];
		
		$validator = Validator::make($input, $validationRules, $errorMessages);
		
		if ($validator->fails()) {			
			return response()->json($validator->errors());
		}
		
		$user = User::firstOrNew(['PhoneNumber' => $input['PhoneNumber']]); //Check user exists or not based on unique id column instead of Phone number
		if ($user->exists) {
			// user already exists
			$error = ['error' => 'User already exists in the system', 'code' => '1'];
			return response()->json($error);
		} else {
			// user created from 'new'; does not exist in database.
			$user->UserName 			= $input['UserName'];
			$user->Salt 				= bin2hex(random_bytes(32));//$hash["salt"]; // salt
			$password 					= $input['Password'];
			$user->EncryptedPassword 	= password_hash($user->Salt.$password, PASSWORD_BCRYPT); //$hash["encrypted"]; // encrypted password
			$user->Country				= $input['Country'];
			$user->PhoneNumber			= $input['PhoneNumber'];
			$user->EmailID				= $input['EmailID'];
			$user->save();
			return response()->json($user);
		}
		
		
		//return "Validation Sucessful";
	}
	
	public function sendemail($id)
	{
		//$input = $request->all();
		$code = random_int(100000, 999999);
		//$user = User::firstOrNew(['PhoneNumber' => $input['PhoneNumber']]);
		//$user = User::where('PhoneNumber',$input['PhoneNumber']).get();
		//if ($user->exists) {
		$user = User::find($id);
		if(empty($user)) {
			$error = ['error' => 'PhoneNumber Not Found', 'code' => '1'];
			return response()->json($error);
		}
		else {
			$to = $user->EmailID;
			$subject = "E2E Secure Chat : Verification Code";			 
			$message = "<b>Please Enter below code to verify and continue chatting</b>";
			$message .= "Your Verification code is ";
			$message .=$code;
			$header = "From:bandini.bhopi@student.csulb.edu \r\n";
			$header .= "Cc:\r\n";
			$header .= "MIME-Version: 1.0\r\n";
			$header .= "Content-type: text/html\r\n";
			$retval=mail($to,$subject,$message,$header);
			if( $retval == true ) {
				$emailstatus = ['Email Sent Successfully'];
				return response()->json($error);
			 }else {
				$emailstatus = ['Email Not Sent Successfully'];
				return response()->json($error);
			 }
		}
		
         
        
		//$error = ['error' => 'Email Sent Successfully', 'code' => '1'];
		//return response()->json($error);
		//return "In SendEmail Function : Start";
	}
}
