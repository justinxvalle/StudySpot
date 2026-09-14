import { useState, useEffect, useCallback } from "react";
import { type Spot, type AmenityScore, type Report } from "./types";
import { MapContainer, TileLayer, Marker, Popup } from "react-leaflet";
import ReportForm from "./ReportForm";

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

  const loadSpots = useCallback(async () => {
    try {
      setError(null);

      const res = await fetch(`${import.meta.env.VITE_API_URL}/api/spots`);

      if (!res.ok) {
        const body = await res.json().catch(() => null);
        throw new Error(
          body?.message ?? `Could not load spots (HTTP ${res.status})`,
        );
      }

      const data: Spot[] = await res.json();
      setSpots(data);
    } catch (err) {
      setError(err instanceof Error ? err.message : "Could not load spots");
    }
  }, []);

  const submitReport = async (spotId: Spot["spotId"], report: Report) => {
    try {
      setError(null);

      const res = await fetch(
        `${import.meta.env.VITE_API_URL}/api/spots/${spotId}/reports`,
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify(toRequest(report, getReporterId())),
        },
      );

      if (!res.ok) {
        const body = await res.json().catch(() => null);
        throw new Error(
          body?.message ?? `Could not submit report (HTTP ${res.status})`,
        );
      }

      await loadSpots();
    } catch (err) {
      setError(err instanceof Error ? err.message : "Could not submit report");
    }
  };

  useEffect(() => {
    void loadSpots();
  }, [loadSpots]);

  return (
    <div>
      {error && <p role="alert">Error: {error}</p>}
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
              <ReportForm
                onSubmit={(report) => submitReport(spot.spotId, report)}
              />
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

function toRequest(report: Report, reporterId: string) {
  return {
    outletLevel: report.outlets || null,
    noise: report.noise || null,
    hasWifi: report.wifi === "" ? null : report.wifi === "YES",
    comments: report.comments || null,
    reporterId,
  };
}
export default App;
