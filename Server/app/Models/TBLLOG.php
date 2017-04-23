<?php

/**
 * Created by Reliese Model.
 * Date: Sat, 01 Apr 2017 22:41:17 +0000.
 */

namespace App\Models;

use Reliese\Database\Eloquent\Model as Eloquent;

/**
 * Class TblLog
 * 
 * @property int $ID
 * @property string $ErrorCode
 * @property string $ErrorMsg
 * @property string $ModuleName
 * @property string $PageName
 * @property string $FunctionName
 * @property \Carbon\Carbon $LogDateTime
 * @property int $CreatedBy
 * @property \Carbon\Carbon $CreatedOn
 * @property int $ModifiedBy
 * @property \Carbon\Carbon $ModifiedOn
 * @property bool $IsActive
 * @property bool $IsDeleted
 *
 * @package App\Models
 */
class TblLog extends Eloquent
{
	protected $table = 'tbl_log';
	protected $primaryKey = 'ID';
	public $timestamps = false;

	protected $casts = [
		'CreatedBy' => 'int',
		'ModifiedBy' => 'int',
		'IsActive' => 'bool',
		'IsDeleted' => 'bool'
	];

	protected $dates = [
		'LogDateTime',
		'CreatedOn',
		'ModifiedOn'
	];

	protected $fillable = [
		'ErrorCode',
		'ErrorMsg',
		'ModuleName',
		'PageName',
		'FunctionName',
		'LogDateTime',
		'CreatedBy',
		'CreatedOn',
		'ModifiedBy',
		'ModifiedOn',
		'IsActive',
		'IsDeleted'
	];
}
