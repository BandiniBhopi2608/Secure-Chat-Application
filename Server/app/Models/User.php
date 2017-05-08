<?php

/**
 * Created by Reliese Model.
 * Date: Sat, 01 Apr 2017 22:41:17 +0000.
 */

namespace App\Models;

use Illuminate\Auth\Authenticatable;
use Reliese\Database\Eloquent\Model as Eloquent;
use Illuminate\Contracts\Auth\Authenticatable as AuthenticatableContract;
use Illuminate\Contracts\Auth\Access\Authorizable as AuthorizableContract;

/**
 * Class User
 * 
 * @property int $ID
 * @property string $UserName
 * @property string $Salt
 * @property string $EncryptedPassword
 * @property int $Country
 * @property string $PhoneNumber
 * @property string $EmailID
 * @property int $CreatedBy
 * @property \Carbon\Carbon $CreatedOn
 * @property int $ModifiedBy
 * @property \Carbon\Carbon $ModifiedOn
 * @property bool $IsActive
 * @property bool $IsDeleted
 *
 * @package App\Models
 */
class User extends Eloquent implements AuthenticatableContract
{
	use Authenticatable;
	
	protected $table = 'User';
	protected $primaryKey = 'ID';
	public $timestamps = false;

	protected $casts = [
		'Country' => 'int',
		'CreatedBy' => 'int',
		'ModifiedBy' => 'int',
		'IsActive' => 'bool',
		'IsDeleted' => 'bool'
	];

	protected $dates = [
		'CreatedOn',
		'ModifiedOn'
	];

	protected $fillable = [
		//'UserName',
		'FirstName',
		'LastName',
		'EncryptedPassword',
		'Country',
		'PhoneNumber',
		'EmailID',
		'CreatedBy',
		'CreatedOn',
		'ModifiedBy',
		'ModifiedOn',
		'IsActive',
		'IsDeleted'
	];
	
	protected $hidden = [
		'Salt',
		'EncryptedPassword',
		'VerificationCode',
		'IsVerified'
	];
}