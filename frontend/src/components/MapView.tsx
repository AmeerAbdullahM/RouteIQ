import { useEffect } from 'react';
import { MapContainer, TileLayer, Polyline, Marker, Popup, useMap } from 'react-leaflet';
import 'leaflet/dist/leaflet.css';
import L from 'leaflet';

// Fix for default marker icons in React-Leaflet
import icon from 'leaflet/dist/images/marker-icon.png';
import iconShadow from 'leaflet/dist/images/marker-shadow.png';
let DefaultIcon = L.icon({
    iconUrl: icon,
    shadowUrl: iconShadow,
    iconAnchor: [12, 41]
});
L.Marker.prototype.options.icon = DefaultIcon;

const MapUpdater = ({ routeData }: { routeData: any }) => {
  const map = useMap();

  useEffect(() => {
    if (routeData && routeData.candidateRoutes && routeData.candidateRoutes.length > 0) {
      const selected = routeData.candidateRoutes.find((r: any) => r.id === routeData.selectedRouteId) || routeData.candidateRoutes[0];
      const coords = selected.geometryCoordinates.map((c: number[]) => [c[1], c[0]]); // Leaflet uses [lat, lon], OSRM returns [lon, lat]
      
      if (coords.length > 0) {
        const bounds = L.latLngBounds(coords);
        map.fitBounds(bounds, { padding: [50, 50] });
      }
    }
  }, [routeData, map]);

  return null;
};

const CurrentLocationFlyer = ({ currentLocation, hasRoute }: { currentLocation: any; hasRoute: boolean }) => {
  const map = useMap();

  useEffect(() => {
    if (currentLocation && !hasRoute) {
      map.flyTo([currentLocation.latitude, currentLocation.longitude], 15, { animate: true, duration: 1.5 });
    }
  }, [currentLocation, hasRoute, map]);

  return null;
};

const blueDotIcon = L.divIcon({
  className: 'user-gps-dot',
  html: `
    <div style="position: relative; width: 24px; height: 24px; display: flex; align-items: center; justify-content: center;">
      <div style="position: absolute; width: 24px; height: 24px; border-radius: 50%; background: rgba(0, 242, 254, 0.4); animation: pulse-ring 2s cubic-bezier(0.215, 0.61, 0.355, 1) infinite;"></div>
      <div style="width: 14px; height: 14px; border-radius: 50%; background: #00f2fe; border: 2.5px solid #ffffff; box-shadow: 0 0 10px rgba(0, 242, 254, 0.9); z-index: 2;"></div>
    </div>
  `,
  iconSize: [24, 24],
  iconAnchor: [12, 12]
});

interface MapViewProps {
  routeData: any;
  currentLocation?: {
    latitude: number;
    longitude: number;
    name?: string;
  } | null;
}

const MapView = ({ routeData, currentLocation }: MapViewProps) => {
  const position: [number, number] = [11.0168, 76.9558]; // Coimbatore center

  return (
    <div style={{ width: '100%', height: '100%', position: 'absolute', top: 0, left: 0 }}>
      <MapContainer center={position} zoom={13} style={{ height: '100%', width: '100%' }} zoomControl={false}>
        <TileLayer
          attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
          url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
        />

        {currentLocation && (!routeData || !routeData.candidateRoutes || routeData.candidateRoutes.length === 0) && (
          <Marker position={[currentLocation.latitude, currentLocation.longitude]} icon={blueDotIcon}>
            <Popup>
              <div style={{ color: '#111', fontWeight: 600, padding: '4px' }}>
                📍 Your Current Location
                {currentLocation.name && (
                  <div style={{ fontSize: '11px', fontWeight: 400, color: '#555', marginTop: '4px', maxWidth: '200px' }}>
                    {currentLocation.name}
                  </div>
                )}
              </div>
            </Popup>
          </Marker>
        )}

        <CurrentLocationFlyer 
          currentLocation={currentLocation} 
          hasRoute={Boolean(routeData && routeData.candidateRoutes && routeData.candidateRoutes.length > 0)} 
        />
        
        {routeData && routeData.candidateRoutes && routeData.candidateRoutes.map((route: any) => {
          const isSelected = route.id === routeData.selectedRouteId;
          const coords = route.geometryCoordinates.map((c: number[]) => [c[1], c[0]]);
          
          return (
            <Polyline 
              key={route.id} 
              positions={coords} 
              pathOptions={{ 
                color: isSelected ? 'var(--accent-color)' : '#555', 
                weight: isSelected ? 6 : 3,
                opacity: isSelected ? 0.9 : 0.5
              }} 
            />
          );
        })}

        {routeData && routeData.candidateRoutes && routeData.candidateRoutes.length > 0 && (
          <>
            <Marker position={[
              routeData.candidateRoutes[0].geometryCoordinates[0][1], 
              routeData.candidateRoutes[0].geometryCoordinates[0][0]
            ]}>
              <Popup>Origin</Popup>
            </Marker>
            <Marker position={[
              routeData.candidateRoutes[0].geometryCoordinates[routeData.candidateRoutes[0].geometryCoordinates.length-1][1], 
              routeData.candidateRoutes[0].geometryCoordinates[routeData.candidateRoutes[0].geometryCoordinates.length-1][0]
            ]}>
              <Popup>Destination</Popup>
            </Marker>
          </>
        )}

        <MapUpdater routeData={routeData} />
      </MapContainer>
      
      {/* Custom Map Overlay for Info */}
      {routeData && (
        <div className="glass-panel" style={{ 
          position: 'absolute', 
          bottom: '24px', 
          right: '24px', 
          padding: '16px', 
          zIndex: 1000,
          minWidth: '250px'
        }}>
          <h3 style={{ fontSize: '16px', marginBottom: '8px', color: 'var(--accent-color)' }}>Route Summary</h3>
          {(() => {
            const selected = routeData.candidateRoutes.find((r: any) => r.id === routeData.selectedRouteId);
            if (!selected) return null;
            
            const mins = Math.floor(selected.adjustedDurationSeconds / 60);
            return (
              <>
                <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '4px' }}>
                  <span style={{ color: 'var(--text-secondary)' }}>Distance:</span>
                  <strong>{selected.distanceKm.toFixed(2)} km</strong>
                </div>
                <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '4px' }}>
                  <span style={{ color: 'var(--text-secondary)' }}>Est. Time:</span>
                  <strong>{mins} mins</strong>
                </div>
                <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '12px' }}>
                  <span style={{ color: 'var(--text-secondary)' }}>Condition:</span>
                  <span style={{ color: 'var(--success-color)' }}>Optimal</span>
                </div>
                <p style={{ fontSize: '12px', color: 'var(--text-secondary)', lineHeight: '1.4' }}>
                  {selected.explanation}
                </p>
              </>
            );
          })()}
        </div>
      )}
    </div>
  );
};

export default MapView;
