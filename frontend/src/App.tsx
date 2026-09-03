import { useState, useEffect } from "react";
import { type Spot, type AmenityScore } from "./types";
import { MapContainer, TileLayer, Marker, Popup } from "react-leaflet";

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
          <Marker key={spot.spotId} position={[spot.latitude, spot.longitude]}>
            <Popup>
              <strong>{spot.spotName}</strong>
              <div className="spot-address">{spot.address}</div>
              <div className="spot-amenities">
                <div className="spot-outlets">
                  Outlets: {describeOutlets(spot.outlets)}
                  {spot.outlets &&
                    ` (${describeReportCount(spot.outlets.reportCount)})`}
                </div>
                <div className="spot-noise">
                  Noise: {describeNoise(spot.noise)}
                  {spot.noise &&
                    ` (${describeReportCount(spot.noise.reportCount)})`}
                </div>
                <div className="spot-wifi">
                  WiFi: {describeWiFi(spot.wifi)}
                  {spot.wifi &&
                    ` (${describeReportCount(spot.wifi.reportCount)})`}
                </div>
              </div>
              <div className="spot-chain">
                Chain: {spot.chain || "Independent"}
              </div>
              <div className="spot-map-link">
                <a
                  href={spot.mapLink}
                  target="_blank"
                  rel="noopener noreferrer"
                >
                  View on Map
                </a>
              </div>
            </Popup>
          </Marker>
        ))}
      </MapContainer>
    </div>
  );
}

function describeOutlets(score: AmenityScore | null): string {
  if (!score) return "No reports yet";
  if (score.value < 0.25) return "No outlets";
  if (score.value < 0.75) return "Some outlets";
  return "Lots of outlets";
}

function describeNoise(score: AmenityScore | null): string {
  if (!score) return "No reports yet";
  if (score.value < 0.25) return "Very quiet";
  if (score.value < 0.75) return "Moderate noise";
  return "Very noisy";
}

function describeWiFi(score: AmenityScore | null): string {
  if (!score) return "No reports yet";
  if (score.value < 0.5) return "No WiFi";
  return "WiFi available";
}

function describeReportCount(reportCount: number | undefined): string {
  if (reportCount === undefined || reportCount === 0) return "No reports yet";
  if (reportCount === 1) return "1 report";
  return `${reportCount} reports`;
}

export default App;
