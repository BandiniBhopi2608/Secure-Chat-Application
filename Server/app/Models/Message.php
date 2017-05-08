<?php

namespace App\Models;

use Reliese\Database\Eloquent\Model as Eloquent;

class Message extends Eloquent
{
	protected $table = 'message';
	protected $primaryKey = 'ID';
	public $timestamps = false;
	
	
	protected $fillable = [		
		'To',
		'From',
		'Message',
		'Status', //1 - Pending, 2 - Delivered
		'Signature',
		'SendOn'
	];
}