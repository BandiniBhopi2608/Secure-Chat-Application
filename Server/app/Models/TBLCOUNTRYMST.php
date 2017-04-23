<?php

/**
 * Created by Reliese Model.
 * Date: Sat, 01 Apr 2017 22:41:17 +0000.
 */

namespace App\Models;

use Reliese\Database\Eloquent\Model as Eloquent;

/**
 * Class TblCountryMst
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
 * @package App\Models
 */
class TblCountryMst extends Eloquent
{
	protected $table = 'tbl_country_mst';
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
}
