<?php

namespace App\Http\Controllers;

use JWTAuth;
use Tymon\JWTAuth\Exceptions\JWTException;
use App\Models\User;
use App\Models\Message;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Validator;

class MessageController extends Controller
{
	/*
	Desc: This function receives the request and put the message in database for receiver.
		  It does not actually send the message to receiver.
	Response :
	Error Code 1 : Parameters are missing.
	Error Code 2: Sender ID is either invaid or not verified
	Error Code 3: Receiver ID is either invaid or not verified
	*/
	public function SendMessage(Request $request) {
		JWTAuth::parseToken()->authenticate();
		
		$input = $request->all();
		$validationRules = [
			'To' 		=> 'required',
			'From'		=> 'required',
			'Message'	=> 'required',
			'SendOn'	=> 'required'
		];
		$validator = Validator::make($input, $validationRules);		
		if ($validator->fails()) {	
			$errors = ['error' => 'Missing Parameters', 'code' => '1'];		
			return response()->json($errors);
		}
		else {
			$sender = User::find($input['From']);
			if(empty($sender) || $sender->IsVerified == false) {
				$error = ['error' => 'Sender ID is either invaid or not verified', 'code' => '2'];
				return response()->json($error);
			}
			else {
				$receiver = User::find($input['To']);
				if(empty($receiver) || $receiver->IsVerified == false) {
					$error = ['error' => 'Receiver ID is either invaid or not verified', 'code' => '3'];
					return response()->json($error);
				}
				else {
					$message = new Message();
					$message->To 		= $input['To'];
					$message->From 		= $input['From'];
					$message->Message 	= trim($input['Message']);
					$message->Status	= '1';
					$message->SendOn 	= $input['SendOn'];
					$message->Signature	= $input['Signature'];
					$message->save();
					return response()->json($message);
				}
			}
		}
	}
	
		
		public function GetMessage(Request $request,$id, $mid) {
			JWTAuth::parseToken()->authenticate();
			
			$receiver = User::find($id);
			if(empty($receiver) || $receiver->IsVerified == false) {
				$error = ['error' => 'Receiver ID is either invaid or not verified', 'code' => '2'];
				return response()->json($error);
			}
			else {
					$messages = Message::where('To', $id)
										->where('ID', '>', $mid)
										->where('Status','1')
										->orderBy('SendOn', 'asc')
										->get();
					for($i = 0, $size = count($messages); $i < $size; ++$i) {
						$messages[$i]->Status	= '2';
						$messages[$i]->save();
					}
					return response()->json($messages);
			}
		}
	
}