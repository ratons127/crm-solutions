export default function StatusField({ status }: { status: boolean }) {
  return (
    <span
      className={`inline-flex items-center justify-center gap-1.5 px-2 py-1 rounded text-xs font-medium w-20
        ${
          status ? 'bg-green-100 text-green-700' : 'bg-gray-100 text-gray-600'
        }`}
    >
      {status ? 'Enabled' : 'Disabled'}
    </span>
  );
}
