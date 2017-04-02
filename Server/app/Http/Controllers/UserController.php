<?php

namespace App\Http\Controllers;

use App\Models\User;
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
			$errors = ['error' => 'User Not Found', 'code' => '1'];
					return response()->json($errors);
		}
		else {
			return response()->json($user);
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
		
		$user = User::firstOrNew(['PhoneNumber' => $input['PhoneNumber']]);
		if ($user->exists) {
			// user already exists
		} else {
			// user created from 'new'; does not exist in database.
			$user->UserName 			= $input['UserName'];
			$user->Salt 				= bin2hex(random_bytes(32));//$hash["salt"]; // salt
			$password 					= $input['Password'];
			$user->EncryptedPassword 	= hash('sha512', $user->Salt.$password); //$hash["encrypted"]; // encrypted password
			$user->Country				= $input['Country'];
			$user->PhoneNumber			= $input['PhoneNumber'];
			$user->EmailID				= $input['EmailID'];
		}
		$user->save();
		return response()->json($user);
		
		//return "Validation Sucessful";
	}
}
