import { useState } from 'react';
import Sidebar from '../components/Sidebar';
import MapView from '../components/MapView.tsx';

export interface CurrentLocationInfo {
  latitude: number;
  longitude: number;
  name?: string;
}

const Dashboard = () => {
  const [routeData, setRouteData] = useState<any>(null);
  const [currentLocation, setCurrentLocation] = useState<CurrentLocationInfo | null>(null);

  return (
    <div style={{ display: 'flex', width: '100vw', height: '100vh' }}>
      <Sidebar 
        setRouteData={setRouteData} 
        onCurrentLocationChange={setCurrentLocation} 
      />
      <MapView 
        routeData={routeData} 
        currentLocation={currentLocation} 
      />
    </div>
  );
};

export default Dashboard;
