export default function PublicLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return <main className="px-0 min-h-screen">{children}</main>;
}
