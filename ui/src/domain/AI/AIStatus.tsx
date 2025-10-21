import React, { useState, useEffect } from 'react';
import { Card, Descriptions, Tag, Space, Button, Alert } from 'antd';
import { RobotOutlined, CheckCircleOutlined, CloseCircleOutlined } from '@ant-design/icons';
import axios from 'axios';

interface AIStatus {
  enabled: boolean;
  provider: string;
  recommendationsEnabled: boolean;
  autoUpdateEnabled: boolean;
  optimizationsEnabled: boolean;
}

const AIStatus: React.FC = () => {
  const [status, setStatus] = useState<AIStatus | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const fetchStatus = async () => {
    setLoading(true);
    setError(null);
    try {
      const response = await axios.get('/api/v1/ai/status');
      setStatus(response.data);
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to load AI status');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchStatus();
  }, []);

  const StatusTag = ({ enabled }: { enabled: boolean }) => (
    <Tag icon={enabled ? <CheckCircleOutlined /> : <CloseCircleOutlined />} color={enabled ? 'success' : 'default'}>
      {enabled ? 'Enabled' : 'Disabled'}
    </Tag>
  );

  return (
    <Card
      title={
        <Space>
          <RobotOutlined />
          AI Features Status
        </Space>
      }
      extra={<Button onClick={fetchStatus} loading={loading}>Refresh</Button>}
    >
      {error && (
        <Alert
          message="Error"
          description={error}
          type="error"
          showIcon
          closable
          onClose={() => setError(null)}
          style={{ marginBottom: 16 }}
        />
      )}
      
      {status && (
        <Descriptions bordered column={1}>
          <Descriptions.Item label="AI Features">
            <StatusTag enabled={status.enabled} />
          </Descriptions.Item>
          <Descriptions.Item label="AI Provider">
            <Tag color="blue">{status.provider}</Tag>
          </Descriptions.Item>
          <Descriptions.Item label="Module Recommendations">
            <StatusTag enabled={status.recommendationsEnabled} />
          </Descriptions.Item>
          <Descriptions.Item label="Auto-Update">
            <StatusTag enabled={status.autoUpdateEnabled} />
          </Descriptions.Item>
          <Descriptions.Item label="Optimization Suggestions">
            <StatusTag enabled={status.optimizationsEnabled} />
          </Descriptions.Item>
        </Descriptions>
      )}
    </Card>
  );
};

export default AIStatus;
