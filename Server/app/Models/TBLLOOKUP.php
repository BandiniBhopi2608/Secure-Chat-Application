<?php

/**
 * Created by Reliese Model.
 * Date: Sat, 01 Apr 2017 22:41:17 +0000.
 */

namespace App\Models;

use Reliese\Database\Eloquent\Model as Eloquent;

/**
 * Class TblLookup
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
 * @package App\Models
 */
class TblLookup extends Eloquent
{
	protected $table = 'tbl_lookup';
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
}
