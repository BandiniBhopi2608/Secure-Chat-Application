<?php

/**
 * Created by Reliese Model.
 * Date: Fri, 31 Mar 2017 02:03:03 +0000.
 */

namespace App\Models;

use Reliese\Database\Eloquent\Model as Eloquent;

/**
 * Class TBLLOOKUP
 * 
 * @property int $ID
 * @property int $Type
 * @property int $ParentKey
 * @property string $Code
 * @property string $Description
 * @property int $SortOrder
 * @property int $CreatedBy
 * @property \Carbon\Carbon $CreatedOn
 * @property int $ModifiedBy
 * @property \Carbon\Carbon $ModifiedOn
 * @property bool $IsActive
 * @property bool $IsDeleted
 * 
 * @property \App\Models\TBLUSER $t_b_l_u_s_e_r
 * @property \Illuminate\Database\Eloquent\Collection $t_b_l__c_o_u_n_t_r_y__m_s_t_s
 *
 * @package App\Models
 */
class TBLLOOKUP extends Eloquent
{
	protected $table = 'TBL_LOOKUP';
	protected $primaryKey = 'ID';
	public $timestamps = false;

	protected $casts = [
		'Type' => 'int',
		'ParentKey' => 'int',
		'SortOrder' => 'int',
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
		'Type',
		'ParentKey',
		'Code',
		'Description',
		'SortOrder',
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

	public function t_b_l__c_o_u_n_t_r_y__m_s_t_s()
	{
		return $this->hasMany(\App\Models\TBLCOUNTRYMST::class, 'ISOCode', 'Code');
	}
}
