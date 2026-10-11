import { useState } from 'react';
import { Navigation } from 'lucide-react';
import axios from 'axios';
import PlaceAutocomplete, { type LocationItem } from './PlaceAutocomplete';

interface SidebarProps {
  setRouteData: any;
  onCurrentLocationChange?: (loc: { latitude: number; longitude: number; name?: string } | null) => void;
}

const Sidebar = ({ setRouteData, onCurrentLocationChange }: SidebarProps) => {
  const [origin, setOrigin] = useState('');
  const [destination, setDestination] = useState('');
  const [originLocation, setOriginLocation] = useState<LocationItem | null>(null);
  const [destLocation, setDestLocation] = useState<LocationItem | null>(null);
  const [loading, setLoading] = useState(false);
  const [mode, setMode] = useState('DRIVING');
  const [optMode, setOptMode] = useState('BALANCED');

  const handleCalculate = async () => {
    if (!origin.trim() || !destination.trim()) {
      alert('Please enter both origin and destination locations');
      return;
    }
    setLoading(true);
    try {
      // Use pre-selected location or fallback to geocode search
      let originLoc = originLocation;
      if (!originLoc || (originLoc.displayName !== origin && origin !== 'Your current location')) {
        const originRes = await axios.get(`http://localhost:8080/api/v1/geocoding/search?q=${encodeURIComponent(origin.trim())}`);
        if (!originRes.data || originRes.data.length === 0) {
          alert('Could not find location for origin');
          setLoading(false);
          return;
        }
        originLoc = originRes.data[0];
      }

      let destLoc = destLocation;
      if (!destLoc || destLoc.displayName !== destination) {
        const destRes = await axios.get(`http://localhost:8080/api/v1/geocoding/search?q=${encodeURIComponent(destination.trim())}`);
        if (!destRes.data || destRes.data.length === 0) {
          alert('Could not find location for destination');
          setLoading(false);
          return;
        }
        destLoc = destRes.data[0];
      }
      
      const payload = {
        origin: { name: originLoc.name || origin, latitude: originLoc.latitude, longitude: originLoc.longitude },
        destination: { name: destLoc.name || destination, latitude: destLoc.latitude, longitude: destLoc.longitude },
        travelMode: mode,
        optimizationMode: optMode,
        trafficMode: 'SIMULATED',
        avoidTolls: false,
        avoidClosures: false
      };

      const res = await axios.post('http://localhost:8080/api/v1/routes/calculate', payload, {
        headers: { 'Content-Type': 'application/json' }
      });
      
      setRouteData(res.data);
    } catch (error) {
      console.error('Failed to calculate route', error);
      alert('Failed to connect to backend or route error.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="glass-panel" style={{ 
      width: '380px', 
      height: 'calc(100vh - 32px)', 
      margin: '16px', 
      display: 'flex', 
      flexDirection: 'column', 
      position: 'absolute', 
      zIndex: 1000 
    }}>
      <div style={{ padding: '20px 24px', borderBottom: '1px solid var(--panel-border)' }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: '12px' }}>
          <Navigation color="var(--accent-color)" size={24} />
          <div>
            <h1 style={{ fontSize: '20px', fontWeight: 700, margin: 0, color: 'var(--text-primary)', lineHeight: 1.2 }}>
              RouteIQ
            </h1>
            <span style={{ fontSize: '12px', color: 'var(--text-secondary)' }}>Route Settings</span>
          </div>
        </div>
      </div>

      <div style={{ padding: '24px', flex: 1, overflowY: 'visible' }}>
        <PlaceAutocomplete
          label="Origin"
          placeholder="Enter origin or choose current location..."
          value={origin}
          onChange={val => {
            setOrigin(val);
            if (originLocation && originLocation.displayName !== val) {
              setOriginLocation(null);
              onCurrentLocationChange?.(null);
            }
          }}
          onSelect={item => {
            setOriginLocation(item);
          }}
          iconColor="var(--accent-color)"
          showCurrentLocationOption={true}
          onCurrentLocationSelect={item => {
            setOriginLocation(item);
            onCurrentLocationChange?.(item);
          }}
        />

        <div style={{ marginTop: '20px' }}>
          <PlaceAutocomplete
            label="Destination"
            placeholder="Enter destination city or place..."
            value={destination}
            onChange={val => {
              setDestination(val);
              if (destLocation && destLocation.displayName !== val) {
                setDestLocation(null);
              }
            }}
            onSelect={item => setDestLocation(item)}
            iconColor="#ff7675"
          />
        </div>

        <div className="input-group" style={{ marginTop: '24px' }}>
          <label>Travel Mode</label>
          <div style={{ display: 'flex', gap: '8px' }}>
            {['DRIVING', 'WALKING', 'CYCLING'].map(m => (
              <button 
                key={m}
                className={`btn ${mode === m ? '' : 'btn-secondary'}`}
                style={{ flex: 1, fontSize: '12px', padding: '8px' }}
                onClick={() => setMode(m)}
              >
                {m}
              </button>
            ))}
          </div>
        </div>

        <div className="input-group" style={{ marginTop: '16px' }}>
          <label>Optimization Goal</label>
          <select 
            className="input-field"
            value={optMode}
            onChange={e => setOptMode(e.target.value)}
          >
            <option value="FASTEST">Fastest (Time)</option>
            <option value="SHORTEST">Shortest (Distance)</option>
            <option value="BALANCED">Balanced</option>
            <option value="FUEL">Eco-friendly (Fuel)</option>
          </select>
        </div>
      </div>

      <div style={{ padding: '24px', borderTop: '1px solid var(--panel-border)' }}>
        <button className="btn" style={{ width: '100%' }} onClick={handleCalculate} disabled={loading}>
          {loading ? <div className="spinner"></div> : 'Calculate Route'}
        </button>
      </div>
    </div>
  );
};

export default Sidebar;
