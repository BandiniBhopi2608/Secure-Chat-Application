<?php

namespace App\Models;

use Reliese\Database\Eloquent\Model as Eloquent;
class Challenge extends Eloquent
{
	protected $table = 'challenge';
	public $timestamps = false;
	
	protected $fillable = [
		'Challenge',
		'PhoneNumber',
		'ChlngTimeStamp' //Use this field in second phase
	];
}