<?php

/**
 * Created by Reliese Model.
 * Date: Fri, 31 Mar 2017 02:03:03 +0000.
 */

namespace App\Models;

use Reliese\Database\Eloquent\Model as Eloquent;

/**
 * Class TBLLOG
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
 * @property \App\Models\TBLUSER $t_b_l_u_s_e_r
 *
 * @package App\Models
 */
class TBLLOG extends Eloquent
{
	protected $table = 'TBL_LOG';
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

	public function t_b_l_u_s_e_r()
	{
		return $this->belongsTo(\App\Models\TBLUSER::class, 'ModifiedBy');
	}
}
