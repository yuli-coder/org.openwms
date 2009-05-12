/*
 * OpenWMS, the open Warehouse Management System
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.openwms.common.dao.core;

import java.util.List;

import org.openwms.common.dao.LocationDao;
import org.openwms.common.domain.Location;
import org.springframework.transaction.annotation.Transactional;

/**
 * A LocationDaoImpl.
 * 
 * @author <a href="heiko.scherrer@gmx.de">Heiko Scherrer</a>
 * @version $Revision: 314 $
 */
public class LocationDaoImpl extends AbstractGenericJpaDao<Location, Long> implements LocationDao {

	@Override
	String getFindAllQuery() {
		return LocationDao.NQ_FIND_ALL;
	}

	@Override
	String getFindByUniqueIdQuery() {
		return LocationDao.NQ_FIND_BY_UNIQUE_QUERY;
	}

	@Transactional(readOnly = true)
	@SuppressWarnings("unchecked")
	public List<Location> getAllLocations() {
		List<Location> list = getJpaTemplate().findByNamedQuery(LocationDao.NQ_FIND_ALL_EAGER);
		for (Location location : list) {
			location.getLocationType();
		}
		return list;
	}

}