import { useState } from "react";

interface Report {
  outlets: string;
  noise: string;
  wifi: string;
  comments: string;
}

function ReportForm({ onSubmit }: { onSubmit: (report: Report) => void }) {
  const [outlets, setOutlets] = useState("");
  const [noise, setNoise] = useState("");
  const [wifi, setWifi] = useState("");
  const [comments, setComments] = useState("");

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!outlets && !noise && !wifi) {
      return;
    }
    onSubmit({ outlets, noise, wifi, comments });
    setOutlets("");
    setNoise("");
    setWifi("");
    setComments("");
  };

  return (
    <form onSubmit={handleSubmit}>
      <select value={outlets} onChange={(e) => setOutlets(e.target.value)}>
        <option value="">Outlets - no answer</option>
        <option value="NONE">None</option>
        <option value="FEW">Few</option>
        <option value="MANY">Many</option>
      </select>

      <select value={noise} onChange={(e) => setNoise(e.target.value)}>
        <option value="">Noise - no answer</option>
        <option value="QUIET">Quiet</option>
        <option value="MODERATE">Moderate</option>
        <option value="LOUD">Loud</option>
      </select>

      <select value={wifi} onChange={(e) => setWifi(e.target.value)}>
        <option value="">WiFi - no answer</option>
        <option value="NO">No</option>
        <option value="YES">Yes</option>
      </select>

      <textarea
        value={comments}
        onChange={(e) => setComments(e.target.value)}
        maxLength={500}
        placeholder="Additional comments (max 500 characters)"
      />

      <button type="submit">Submit report</button>
    </form>
  );
}

export default ReportForm;
