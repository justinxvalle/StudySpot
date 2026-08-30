import { useState, useEffect } from "react";
import { type Spot } from "./types";

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
      <ul>
        {spots.map((s) => (
          <li key={s.spotId}>{s.spotName}</li>
        ))}
      </ul>
    </div>
  );
}

export default App;
