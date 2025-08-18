import { execSync } from 'node:child_process';

export function setup() {
  console.log('Executando setup global dos testes...');

  execSync('npm run prisma:test:migrate');

  console.log('✅ Banco de dados de TESTE pronto.');

  return () => {
    console.log('🏁 Testes finalizados.');
  };
}
