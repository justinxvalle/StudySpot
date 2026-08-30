import { useState, useEffect } from "react";
import { type Spot } from "./types";
import { MapContainer, TileLayer, Marker } from "react-leaflet";

function App() {
  const [spots, setSpots] = useState<Spot[]>([]);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    fetch(`${import.meta.env.VITE_API_URL}/api/spots`)
      .then((res) => res.json())
      .then((data) => setSpots(data))
      .catch((err) => setError(err.message));
  }, []);

  if (error) return <p>Error: {error}</p>;

  return (
    <div>
      <h1>Spots ({spots.length})</h1>

      <MapContainer
        center={[43.73, -79.3]}
        zoom={12}
        style={{ height: "500px", width: "100%" }}
      >
        <TileLayer
          attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
          url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
        />

        {spots.map((spot) => (
          <Marker
            key={spot.spotId}
            position={[spot.latitude, spot.longitude]}
          />
        ))}
      </MapContainer>
    </div>
  );
}

export default App;
