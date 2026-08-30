import { useState, useEffect } from "react";

function App() {
  const [spots, setSpots] = useState<any[]>([]);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    fetch("http://localhost:8080/api/spots")
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
