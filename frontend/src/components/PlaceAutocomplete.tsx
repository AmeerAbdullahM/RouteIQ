import React, { useState, useEffect, useRef } from 'react';
import { Search, MapPin, Loader2, X, Navigation } from 'lucide-react';
import axios from 'axios';

export interface LocationItem {
  name: string;
  latitude: number;
  longitude: number;
  displayName?: string;
  details?: string;
}

interface PlaceAutocompleteProps {
  label: string;
  placeholder?: string;
  value: string;
  onChange: (val: string) => void;
  onSelect: (item: LocationItem) => void;
  iconColor?: string;
  showCurrentLocationOption?: boolean;
  onCurrentLocationSelect?: (item: LocationItem) => void;
}

export const PlaceAutocomplete: React.FC<PlaceAutocompleteProps> = ({
  label,
  placeholder = 'Search place, city or landmark...',
  value,
  onChange,
  onSelect,
  iconColor = 'var(--text-secondary)',
  showCurrentLocationOption = false,
  onCurrentLocationSelect
}) => {
  const [suggestions, setSuggestions] = useState<LocationItem[]>([]);
  const [isOpen, setIsOpen] = useState(false);
  const [loading, setLoading] = useState(false);
  const [isLocating, setIsLocating] = useState(false);
  const [selectedIndex, setSelectedIndex] = useState(-1);
  const containerRef = useRef<HTMLDivElement>(null);
  const debounceTimerRef = useRef<NodeJS.Timeout | null>(null);

  // Close dropdown on outside click
  useEffect(() => {
    const handleClickOutside = (e: MouseEvent) => {
      if (containerRef.current && !containerRef.current.contains(e.target as Node)) {
        setIsOpen(false);
      }
    };
    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, []);

  // Fetch suggestions with debouncing
  useEffect(() => {
    if (!value || value.trim().length < 2) {
      setSuggestions([]);
      // If current location option is allowed, keep dropdown open if user focused
      if (!showCurrentLocationOption) {
        setIsOpen(false);
      }
      return;
    }

    if (debounceTimerRef.current) {
      clearTimeout(debounceTimerRef.current);
    }

    debounceTimerRef.current = setTimeout(async () => {
      setLoading(true);
      try {
        // Try backend autocomplete first
        const res = await axios.get(
          `http://localhost:8080/api/v1/geocoding/autocomplete?q=${encodeURIComponent(value.trim())}`
        );
        if (res.data && Array.isArray(res.data) && res.data.length > 0) {
          setSuggestions(res.data);
          setIsOpen(true);
          setSelectedIndex(-1);
        } else {
          // Direct fallback to photon if backend returned empty
          const fallbackRes = await axios.get(
            `https://photon.komoot.io/api/?q=${encodeURIComponent(value.trim())}&limit=6`
          );
          if (fallbackRes.data && fallbackRes.data.features) {
            const items: LocationItem[] = fallbackRes.data.features.map((f: any) => {
              const p = f.properties;
              const title = p.name || p.city || p.country;
              const details = [p.city !== title ? p.city : null, p.state, p.country]
                .filter(Boolean)
                .join(', ');
              return {
                name: details ? `${title}, ${details}` : title,
                displayName: title,
                details: details,
                latitude: f.geometry.coordinates[1],
                longitude: f.geometry.coordinates[0]
              };
            });
            setSuggestions(items);
            setIsOpen(true);
            setSelectedIndex(-1);
          } else {
            setSuggestions([]);
            if (showCurrentLocationOption) setIsOpen(true);
          }
        }
      } catch (err) {
        console.error('Autocomplete fetch error', err);
        // Fallback to photon on error
        try {
          const fallbackRes = await axios.get(
            `https://photon.komoot.io/api/?q=${encodeURIComponent(value.trim())}&limit=6`
          );
          if (fallbackRes.data && fallbackRes.data.features) {
            const items: LocationItem[] = fallbackRes.data.features.map((f: any) => {
              const p = f.properties;
              const title = p.name || p.city || p.country;
              const details = [p.city !== title ? p.city : null, p.state, p.country]
                .filter(Boolean)
                .join(', ');
              return {
                name: details ? `${title}, ${details}` : title,
                displayName: title,
                details: details,
                latitude: f.geometry.coordinates[1],
                longitude: f.geometry.coordinates[0]
              };
            });
            setSuggestions(items);
            setIsOpen(true);
          }
        } catch {
          setSuggestions([]);
          if (showCurrentLocationOption) setIsOpen(true);
        }
      } finally {
        setLoading(false);
      }
    }, 280);

    return () => {
      if (debounceTimerRef.current) {
        clearTimeout(debounceTimerRef.current);
      }
    };
  }, [value, showCurrentLocationOption]);

  const handleSelect = (item: LocationItem) => {
    onChange(item.displayName || item.name);
    onSelect(item);
    setIsOpen(false);
    setSuggestions([]);
  };

  const handleUseCurrentLocation = (e?: React.MouseEvent) => {
    if (e) e.stopPropagation();
    if (!navigator.geolocation) {
      alert('Geolocation is not supported by your browser.');
      return;
    }

    setIsLocating(true);
    navigator.geolocation.getCurrentPosition(
      async (position) => {
        const lat = position.coords.latitude;
        const lon = position.coords.longitude;
        try {
          // Reverse geocode to find address/city name
          const res = await axios.get(
            `http://localhost:8080/api/v1/geocoding/reverse?lat=${lat}&lon=${lon}`
          );
          const data = res.data;
          const locName = data?.displayName || data?.name || 'Your current location';
          const locItem: LocationItem = {
            name: data?.name || `Current Location (${lat.toFixed(4)}, ${lon.toFixed(4)})`,
            displayName: 'Your current location',
            details: data?.details || locName,
            latitude: lat,
            longitude: lon
          };
          onChange('Your current location');
          onSelect(locItem);
          if (onCurrentLocationSelect) {
            onCurrentLocationSelect(locItem);
          }
        } catch (err) {
          console.warn('Reverse geocoding failed, using coordinates', err);
          const locItem: LocationItem = {
            name: `Current Location (${lat.toFixed(4)}, ${lon.toFixed(4)})`,
            displayName: 'Your current location',
            details: `${lat.toFixed(4)}, ${lon.toFixed(4)}`,
            latitude: lat,
            longitude: lon
          };
          onChange('Your current location');
          onSelect(locItem);
          if (onCurrentLocationSelect) {
            onCurrentLocationSelect(locItem);
          }
        } finally {
          setIsLocating(false);
          setIsOpen(false);
        }
      },
      (error) => {
        console.error('Geolocation error:', error);
        let msg = 'Could not access device location.';
        if (error.code === error.PERMISSION_DENIED) {
          msg = 'Location permission was denied. Please allow location access in your browser.';
        } else if (error.code === error.POSITION_UNAVAILABLE) {
          msg = 'Location unavailable. Please check device GPS.';
        } else if (error.code === error.TIMEOUT) {
          msg = 'Location request timed out. Please try again.';
        }
        alert(msg);
        setIsLocating(false);
      },
      { enableHighAccuracy: true, timeout: 10000, maximumAge: 0 }
    );
  };

  const handleKeyDown = (e: React.KeyboardEvent) => {
    if (!isOpen) return;

    if (e.key === 'ArrowDown') {
      e.preventDefault();
      setSelectedIndex(prev => (prev < suggestions.length - 1 ? prev + 1 : 0));
    } else if (e.key === 'ArrowUp') {
      e.preventDefault();
      setSelectedIndex(prev => (prev > 0 ? prev - 1 : suggestions.length - 1));
    } else if (e.key === 'Enter') {
      if (selectedIndex >= 0 && selectedIndex < suggestions.length) {
        e.preventDefault();
        handleSelect(suggestions[selectedIndex]);
      } else if (showCurrentLocationOption && selectedIndex === -1 && suggestions.length === 0) {
        e.preventDefault();
        handleUseCurrentLocation();
      }
    } else if (e.key === 'Escape') {
      setIsOpen(false);
    }
  };

  const showDropdown = isOpen && (showCurrentLocationOption || suggestions.length > 0);

  return (
    <div className="input-group" ref={containerRef} style={{ position: 'relative' }}>
      <label style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
        <span>{label}</span>
      </label>

      <div style={{ position: 'relative', display: 'flex', alignItems: 'center' }}>
        <Search
          size={16}
          style={{
            position: 'absolute',
            left: '12px',
            color: iconColor,
            pointerEvents: 'none',
            zIndex: 2
          }}
        />

        <input
          type="text"
          className="input-field"
          style={{
            paddingLeft: '38px',
            paddingRight: showCurrentLocationOption ? (value ? '56px' : '36px') : (value ? '36px' : '12px'),
            width: '100%'
          }}
          placeholder={placeholder}
          value={value}
          onChange={e => onChange(e.target.value)}
          onFocus={() => {
            setIsOpen(true);
          }}
          onKeyDown={handleKeyDown}
          autoComplete="off"
        />

        {/* Quick GPS button inside input for Origin */}
        {showCurrentLocationOption && (
          <button
            type="button"
            onClick={handleUseCurrentLocation}
            disabled={isLocating}
            style={{
              position: 'absolute',
              right: value ? '32px' : '10px',
              background: 'transparent',
              border: 'none',
              color: 'var(--accent-color)',
              cursor: isLocating ? 'wait' : 'pointer',
              display: 'flex',
              alignItems: 'center',
              padding: '4px',
              borderRadius: '4px',
              zIndex: 3
            }}
            title="Use current device location"
          >
            {isLocating ? (
              <Loader2 size={15} className="spinner" />
            ) : (
              <Navigation size={15} style={{ transform: 'rotate(-45deg)' }} />
            )}
          </button>
        )}

        {loading ? (
          <Loader2
            size={16}
            className="spinner"
            style={{
              position: 'absolute',
              right: '12px',
              color: 'var(--accent-color)',
              zIndex: 3
            }}
          />
        ) : value ? (
          <button
            type="button"
            onClick={() => {
              onChange('');
              setSuggestions([]);
              if (!showCurrentLocationOption) setIsOpen(false);
            }}
            style={{
              position: 'absolute',
              right: '10px',
              background: 'transparent',
              border: 'none',
              color: 'var(--text-secondary)',
              cursor: 'pointer',
              display: 'flex',
              alignItems: 'center',
              padding: '4px',
              borderRadius: '50%',
              zIndex: 3
            }}
            title="Clear"
          >
            <X size={14} />
          </button>
        ) : null}
      </div>

      {/* Google Maps-style Autocomplete Dropdown */}
      {showDropdown && (
        <div
          style={{
            position: 'absolute',
            top: 'calc(100% + 4px)',
            left: 0,
            right: 0,
            background: 'rgba(21, 28, 38, 0.98)',
            backdropFilter: 'blur(16px)',
            border: '1px solid var(--panel-border)',
            borderRadius: '10px',
            boxShadow: '0 12px 32px rgba(0, 0, 0, 0.7), 0 0 1px rgba(102, 252, 241, 0.3)',
            zIndex: 9999,
            overflow: 'hidden',
            maxHeight: '280px',
            overflowY: 'auto'
          }}
        >
          {/* Top Option: "Your current location" */}
          {showCurrentLocationOption && (
            <div
              onClick={handleUseCurrentLocation}
              style={{
                display: 'flex',
                alignItems: 'center',
                gap: '12px',
                padding: '12px 14px',
                cursor: isLocating ? 'wait' : 'pointer',
                background: 'rgba(0, 242, 254, 0.08)',
                borderBottom: '1px solid rgba(102, 252, 241, 0.25)',
                borderLeft: '3px solid var(--accent-color)',
                transition: 'background 0.15s ease'
              }}
              onMouseEnter={e => (e.currentTarget.style.background = 'rgba(0, 242, 254, 0.16)')}
              onMouseLeave={e => (e.currentTarget.style.background = 'rgba(0, 242, 254, 0.08)')}
            >
              <div
                style={{
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'center',
                  width: '28px',
                  height: '28px',
                  borderRadius: '50%',
                  background: 'rgba(0, 242, 254, 0.18)',
                  color: 'var(--accent-color)',
                  flexShrink: 0
                }}
              >
                {isLocating ? (
                  <Loader2 size={16} className="spinner" />
                ) : (
                  <Navigation size={16} style={{ transform: 'rotate(-45deg)' }} />
                )}
              </div>
              <div style={{ flex: 1, overflow: 'hidden' }}>
                <div style={{ fontSize: '13px', fontWeight: 700, color: 'var(--accent-color)' }}>
                  {isLocating ? 'Acquiring device GPS...' : 'Your current location'}
                </div>
                <div style={{ fontSize: '11px', color: 'var(--text-secondary)', marginTop: '2px' }}>
                  Use device GPS coordinates
                </div>
              </div>
            </div>
          )}

          {/* List of Place Suggestions */}
          {suggestions.length > 0 ? (
            suggestions.map((item, index) => {
              const isHighlighted = index === selectedIndex;
              return (
                <div
                  key={`${item.name}-${index}`}
                  onClick={() => handleSelect(item)}
                  onMouseEnter={() => setSelectedIndex(index)}
                  style={{
                    display: 'flex',
                    alignItems: 'center',
                    gap: '12px',
                    padding: '10px 14px',
                    cursor: 'pointer',
                    transition: 'all 0.15s ease',
                    background: isHighlighted ? 'rgba(102, 252, 241, 0.12)' : 'transparent',
                    borderLeft: isHighlighted
                      ? '3px solid var(--accent-color)'
                      : '3px solid transparent',
                    borderBottom:
                      index < suggestions.length - 1
                        ? '1px solid rgba(255, 255, 255, 0.05)'
                        : 'none'
                  }}
                >
                  <div
                    style={{
                      display: 'flex',
                      alignItems: 'center',
                      justifyContent: 'center',
                      width: '28px',
                      height: '28px',
                      borderRadius: '6px',
                      background: 'rgba(102, 252, 241, 0.08)',
                      color: 'var(--accent-color)',
                      flexShrink: 0
                    }}
                  >
                    <MapPin size={16} />
                  </div>
                  <div style={{ overflow: 'hidden', flex: 1 }}>
                    <div
                      style={{
                        fontSize: '13px',
                        fontWeight: 600,
                        color: 'var(--text-primary)',
                        whiteSpace: 'nowrap',
                        overflow: 'hidden',
                        textOverflow: 'ellipsis'
                      }}
                    >
                      {item.displayName || item.name}
                    </div>
                    {item.details && (
                      <div
                        style={{
                          fontSize: '11px',
                          color: 'var(--text-secondary)',
                          whiteSpace: 'nowrap',
                          overflow: 'hidden',
                          textOverflow: 'ellipsis',
                          marginTop: '2px'
                        }}
                      >
                        {item.details}
                      </div>
                    )}
                  </div>
                </div>
              );
            })
          ) : value && value.trim().length >= 2 && !loading ? (
            <div
              style={{
                padding: '12px 16px',
                fontSize: '12px',
                color: 'var(--text-secondary)',
                textAlign: 'center'
              }}
            >
              No matching places found
            </div>
          ) : null}
        </div>
      )}
    </div>
  );
};

export default PlaceAutocomplete;
