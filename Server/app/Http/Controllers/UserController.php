<?php

namespace App\Http\Controllers;
use App\Models\TBLUSER;

use Illuminate\Http\Request;

class UserController extends Controller
{
    public function test(){
		return "Success";
	}
	
	public function getUser($id) {
		
		$user = TBLUSER::find($id);
		if(empty($user)) {
			$errors = ['error' => 'Userv Not Found', 'code' => '1'];
					return response()->json($errors);
		}
		else {
			return response()->json($user);
		}
	}
}

