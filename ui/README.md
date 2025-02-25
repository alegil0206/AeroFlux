# U-space UI

## Overview

This repository contains the user interface for the U-space project. The UI provides a web application for managing drones, geozones, and authorizations. The application is built using React and utilizes an Nginx server for production deployment.

## Usage

### Access the UI

Open a web browser and navigate to the UI.

### Add a Drone

1. Navigate to the **Drones** section.
2. Click on the **Add Drone** button.
3. Fill in the required details for the drone, such as the drone model, owner information, departure point, and arrival point.
4. Click **Save** to add the drone to the system.

### Add a Geozone

1. Navigate to the **Geozones** section.
2. Click on the **Add Geozone** button.
3. Fill in the required details for the geozone, such as the geozone name and category.
4. Specify the shape of the geozone:
    - For a circular geozone, specify the center and radius by moving the pin on the map.
    - For a polygonal geozone, specify the coordinates or draw the shape directly on the map.
5. Click **Save** to add the geozone to the system.
6. Geozones can be activated or deactivated as needed.

### Modify Drones and Geozones

1. Navigate to the respective section (Drones or Geozones).
2. Select the item to edit.
3. Update the necessary details and click **Save** to apply the changes.

### Request Authorization for a Drone to Enter a Geozone

1. Navigate to the **Authorizations** section.
2. Click on the **Request Authorization** button.
3. Select the drone from the list of available drones.
4. Select the geozone from the list of available geozones.
5. Fill in the required information, such as the authorization period in minutes.
6. Click **Submit** to request authorization for the drone to enter the selected geozone.
7. Active authorizations can be revoked prematurely if needed.

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.make an explicit request for maintainers.
