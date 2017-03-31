<?php

/**
 * Created by Reliese Model.
 * Date: Fri, 31 Mar 2017 02:03:03 +0000.
 */

namespace App\Models;

use Reliese\Database\Eloquent\Model as Eloquent;

/**
 * Class TBLCOUNTRYMST
 * 
 * @property int $ID
 * @property string $ISOCode
 * @property string $CountryCode
 * @property int $CreatedBy
 * @property \Carbon\Carbon $CreatedOn
 * @property int $ModifiedBy
 * @property \Carbon\Carbon $ModifiedOn
 * @property bool $IsActive
 * @property bool $IsDeleted
 * 
 * @property \App\Models\TBLUSER $t_b_l_u_s_e_r
 * @property \App\Models\TBLLOOKUP $t_b_l_l_o_o_k_u_p
 * @property \Illuminate\Database\Eloquent\Collection $t_b_l__u_s_e_r_s
 *
 * @package App\Models
 */
class TBLCOUNTRYMST extends Eloquent
{
	protected $table = 'TBL_COUNTRY_MST';
	protected $primaryKey = 'ID';
	public $timestamps = false;

	protected $casts = [
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
		'ISOCode',
		'CountryCode',
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

	public function t_b_l_l_o_o_k_u_p()
	{
		return $this->belongsTo(\App\Models\TBLLOOKUP::class, 'ISOCode', 'Code');
	}

	public function t_b_l__u_s_e_r_s()
	{
		return $this->hasMany(\App\Models\TBLUSER::class, 'Country');
	}
}
