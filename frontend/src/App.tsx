import { useState, useEffect } from "react";
import { type Spot, type AmenityScore } from "./types";
import { MapContainer, TileLayer, Marker, Popup } from "react-leaflet";

function App() {
  const [spots, setSpots] = useState<Spot[]>([]);
  const [error, setError] = useState<string | null>(null);

  const [onlyOutlets, setOnlyOutlets] = useState(false);
  const [onlyQuietSpots, setOnlyQuietSpots] = useState(false);
  const [onlyWifi, setOnlyWifi] = useState(false);
  const [chainFilter, setChainFilter] = useState("");

  const visible = spots.filter((spot) => {
    if (onlyOutlets && (!spot.outlets || spot.outlets.value < 0.25)) {
      return false;
    }
    if (onlyQuietSpots && (!spot.noise || spot.noise.value >= 0.25)) {
      return false;
    }
    if (onlyWifi && (!spot.wifi || spot.wifi.value < 0.5)) {
      return false;
    }
    if (chainFilter && spot.chain !== chainFilter) {
      return false;
    }
    return true;
  });

  const chains = Array.from(
    new Set(
      spots.map((spot) => spot.chain).filter((nullable) => nullable !== null),
    ),
  );

  useEffect(() => {
    fetch(`${import.meta.env.VITE_API_URL}/api/spots`)
      .then((res) => res.json())
      .then((data) => setSpots(data))
      .catch((err) => setError(err.message));
  }, []);

  if (error) return <p>Error: {error}</p>;

  return (
    <div>
      <h1>Spots ({visible.length})</h1>

      <label>
        <input
          type="checkbox"
          checked={onlyOutlets}
          onChange={(e) => setOnlyOutlets(e.target.checked)}
        />
        Only spots with outlets
      </label>

      <label>
        <input
          type="checkbox"
          checked={onlyQuietSpots}
          onChange={(e) => setOnlyQuietSpots(e.target.checked)}
        />
        Only quiet spots
      </label>

      <label>
        <input
          type="checkbox"
          checked={onlyWifi}
          onChange={(e) => setOnlyWifi(e.target.checked)}
        />
        Only spots with WiFi
      </label>

      <select
        value={chainFilter}
        onChange={(e) => setChainFilter(e.target.value)}
      >
        <option value="">All chains</option>
        {chains.map((chain) => (
          <option key={chain} value={chain}>
            {chain}
          </option>
        ))}
      </select>

      <MapContainer
        center={[43.73, -79.3]}
        zoom={12}
        style={{ height: "500px", width: "100%" }}
      >
        <TileLayer
          attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
          url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
        />

        {visible.map((spot) => (
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

function getReporterId(): string {
  let id = localStorage.getItem("reporterId");
  if (!id) {
    id = crypto.randomUUID();
    localStorage.setItem("reporterId", id);
  }
  return id;
}

export default App;
