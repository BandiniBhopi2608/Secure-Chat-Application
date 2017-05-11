<?php
namespace App\Http\Controllers;
use JWTAuth;
use Tymon\JWTAuth\Exceptions\JWTException;
use Mail;
use App\Mail\SendVerificationCode;
use App\Models\User;
use App\Models\Challenge;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Validator;

//Bandini added this controller which handles all user related activities.
class UserController extends Controller
{
    public function test(){
		return "Success";
	}
	/*Response :
	Error Code 1: Sender ID Missing
	Error Code 2: Sender ID is either invaid or not verified
	Error Code 3: Receiver ID is either invaid or not verified
	*/
	public function getUser(Request $request, $id) {
		JWTAuth::parseToken()->authenticate();
		
		$input = $request->all()['nameValuePairs'];
		$validationRules = [ 'SenderID' => 'required'];
		$validator1 = Validator::make($input, $validationRules);
		if ($validator1->fails()) {	
			$errors = ['error' => 'Sender ID missing', 'code' => '1'];		
			return response()->json($errors);
		}
		else {
			$sender = User::find($input['SenderID']);
			if(empty($sender) || $sender->IsVerified == false) {
				$error = ['error' => 'Sender ID is either invaid or not verified', 'code' => '2'];
				return response()->json($error);
			}
			else {
				$receiver = User::find($id);
				if(empty($receiver) || $receiver->IsVerified == false) {
				$error = ['error' => 'Receiver ID is either invaid or not verified', 'code' => '3'];
				return response()->json($error);
				}
				else {
					return response()->json($receiver);
				}
			}
		}
	}
	
	/*Reference: http://www.devnetwork.net/viewtopic.php?f=28&t=38810*/
	public function login(Request $request, $requestNo) {
		$input = $request->all();
		if($requestNo == 1) {
			$validationRules = ['PhoneNumber' => 'required|numeric'];
			$errorMessages = [
				'PhoneNumber.required' => ' The Phone Number field is required.',
				'PhoneNumber.numeric' => ' The Phone Number should contain only numbers.'
			];
			$validator = Validator::make($input, $validationRules, $errorMessages);
			if ($validator->fails()) {			
				return response()->json($validator->errors());
			}
			else {
				$user = User::where('PhoneNumber',$input['PhoneNumber'])->first();
				if(empty($user)) {
					$error = ['error' => 'PhoneNumber Not Found', 'code' => '1'];
					return response()->json($error);
				}
				else {
					if($user->IsVerified == true) {						
						$challenge = bin2hex(random_bytes(32));
						$response = ['Salt' => $user->Salt
									,'Challenge' => $challenge]; //$challenge->Challenge];
						return response()->json($response);
					}
					else {
						$error = ['error' => 'User is not verified.', 'code' => '2'];
						return response()->json($error);
					}
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
				$user = User::where('PhoneNumber',$input['PhoneNumber'])->first();				
				$challenge = $input['Challenge'];			
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
					$mytag = hash_hmac('sha512', $user->EncryptedPassword, $challenge); //Generate tag
					//compare the tag
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
	
	/*
	Response :
	Error Code 1 : Validation Failed
	Error Code 2 : Phone number is not unique. That means User already exits
	Error Code 3 : EmailID is not unique
	Error Code 4 : Could not send Verification Email. Therefore Registration fails.
	*/
	public function register(Request $request)	{		
		$input = $request->all();		
		$validationRules = [
			'FirstName' => 'required|max:50',
			'LastName' => 'required|max:50',
			'Password' => 'required|max:80',
			'Country' => 'required|numeric',
			'PhoneNumber' => 'required|numeric',
			'EmailID' => 'required|email'
		];
		
		$validator1 = Validator::make($input, $validationRules);		
		if ($validator1->fails()) {	
			$errors = ['error' => 'Validation Failed', 'code' => '1'];		
			return response()->json($errors);
		}
		$emailRule = [
			'EmailID' => 'unique:User'
		];
		$validator2 = Validator::make($input, $emailRule);		
		if ($validator2->fails()) {	
			$errors = ['error' => 'EmailID is not unique', 'code' => '3'];		
			return response()->json($errors);
		}		
		//Check user exists or not based on Phone number
		$user = User::firstOrNew(['PhoneNumber' => $input['PhoneNumber'], 'IsActive'=> true, 'IsDeleted'=>false]); 
		if ($user->exists) {
			// user already exists
			$error = ['error' => 'User already exists in the system', 'code' => '2'];
			$errors['ID'] = $user->ID;
			$errors['PhoneNumber'] = $input['PhoneNumber'];
			return response()->json($error);
		}
		else {
			$activationcode = random_int(100000, 999999);
			if($this->sendemail($activationcode, $input['FirstName']. ' ' . $input['LastName'], $input['EmailID'])) {
				$user->FirstName			= $input['FirstName'];
				$user->LastName				= $input['LastName'];
				$user->Salt 				= $input['Salt'];
				$password 					= $input['Password'];
				$user->EncryptedPassword 	= password_hash($user->Salt.$password, PASSWORD_BCRYPT);
				$user->Country				= $input['Country'];
				$user->PhoneNumber			= $input['PhoneNumber'];
				$user->EmailID				= $input['EmailID'];
				$user->IsActive				= false;
				$user->VerificationCode		= $activationcode;
				$user->IsVerified			= false;
				$user->save();
				return response()->json($user);
			
			}
			else {
				$errors = ['error' => 'Error occurred while connecting to Email Server. Check your email or contact administrator', 'code' => '4'];		
				return response()->json($errors);
			}
			
		}
	}
	
	public function sendemail($activationcode, $username, $email )
	{		
		$data = array( 'username' => $username, 'activationcode' => $activationcode );
		try {
			Mail::send('emails.VerificationMail', $data, function($message) use ($email, $username) {						
				$message->to($email, $username)
					->subject('Your Activation Code');
			});
			return true;
		}
		catch(Exception $ex) {
				return false;
		}
	}
	
	/*
	Response :
	Error Code 5: User not found in database.
	Error Code 6: Verification Code does not match.
	*/
	public function verifyuser(Request $request)
	{
		$input = $request->all();
		$user = User::find($input['ID']);
		if(empty($user)) {
			$error = ['error' => 'User Not Found', 'code' => '5'];
			return response()->json($error);
		}
		else {
			if(strcmp($user->VerificationCode, $input['VerificationCode']) == 0) {
				$user->VerificationCode = NULL;
				$user->IsVerified		= true;
				$user->IsActive			= true;				
				$user->save();
				
				//Add Json token
				$token = JWTAuth::fromUser($user);
				$user->token = compact('token')['token'];
				
				return response()->json($user);
			}
			else {
				$error = ['error' => 'Verification Code does not match', 'code' => '6'];
				return response()->json($error);
			}
		}
	}
	
	// For Testing Purpose
	public function getAuthenticatedUser(Request $request)
	{
		JWTAuth::parseToken()->authenticate();
	}
}