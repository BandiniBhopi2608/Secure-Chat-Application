<?php

/**
 * Created by Reliese Model.
 * Date: Fri, 31 Mar 2017 02:03:03 +0000.
 */

namespace App\Models;

use Reliese\Database\Eloquent\Model as Eloquent;

/**
 * Class TBLUSER
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
 * @property \App\Models\TBLCOUNTRYMST $t_b_l_c_o_u_n_t_r_y_m_s_t
 * @property \App\Models\TBLUSER $t_b_l_u_s_e_r
 * @property \Illuminate\Database\Eloquent\Collection $t_b_l__c_o_u_n_t_r_y__m_s_t_s
 * @property \Illuminate\Database\Eloquent\Collection $t_b_l__l_o_g_s
 * @property \Illuminate\Database\Eloquent\Collection $t_b_l__l_o_o_k_u_p_s
 * @property \Illuminate\Database\Eloquent\Collection $t_b_l__u_s_e_r_s
 *
 * @package App\Models
 */
class TBLUSER extends Eloquent
{
	protected $table = 'TBL_USER';
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
		'UserName',
		'Salt',
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

	public function t_b_l_c_o_u_n_t_r_y_m_s_t()
	{
		return $this->belongsTo(\App\Models\TBLCOUNTRYMST::class, 'Country');
	}

	public function t_b_l_u_s_e_r()
	{
		return $this->belongsTo(\App\Models\TBLUSER::class, 'ModifiedBy');
	}

	public function t_b_l__c_o_u_n_t_r_y__m_s_t_s()
	{
		return $this->hasMany(\App\Models\TBLCOUNTRYMST::class, 'ModifiedBy');
	}

	public function t_b_l__l_o_g_s()
	{
		return $this->hasMany(\App\Models\TBLLOG::class, 'ModifiedBy');
	}

	public function t_b_l__l_o_o_k_u_p_s()
	{
		return $this->hasMany(\App\Models\TBLLOOKUP::class, 'ModifiedBy');
	}

	public function t_b_l__u_s_e_r_s()
	{
		return $this->hasMany(\App\Models\TBLUSER::class, 'ModifiedBy');
	}
}
