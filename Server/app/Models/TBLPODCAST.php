<?php

/**
 * Created by Reliese Model.
 * Date: Fri, 31 Mar 2017 02:03:03 +0000.
 */

namespace App\Models;

use Reliese\Database\Eloquent\Model as Eloquent;

/**
 * Class TBLPODCAST
 * 
 * @property int $ID
 * @property string $TITLE
 * @property string $FEED
 * @property \Carbon\Carbon $INSERTION_DATE
 * @property string $DESCRIPTION
 * @property string $LINK_ON_PODCASTPEDIA
 *
 * @package App\Models
 */
class TBLPODCAST extends Eloquent
{
	protected $table = 'TBL_PODCAST';
	protected $primaryKey = 'ID';
	public $timestamps = false;

	protected $dates = [
		'INSERTION_DATE'
	];

	protected $fillable = [
		'TITLE',
		'FEED',
		'INSERTION_DATE',
		'DESCRIPTION',
		'LINK_ON_PODCASTPEDIA'
	];
}
