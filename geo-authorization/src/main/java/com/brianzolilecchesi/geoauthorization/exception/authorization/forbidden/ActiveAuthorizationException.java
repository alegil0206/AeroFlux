package com.brianzolilecchesi.geoauthorization.exception.authorization.forbidden;

public class ActiveAuthorizationException extends ForbiddenAuthorizationRequestException {
	
	private static final long serialVersionUID = 1L;

	public ActiveAuthorizationException(
			Long id,
			String droneId,
			String geozoneId
			) {
		
		super(String.format(
				"Forbidden authorization request: authorization %s is already active for drone %s and geozone %s",
				id,
				droneId,
				geozoneId
				));
	}
}