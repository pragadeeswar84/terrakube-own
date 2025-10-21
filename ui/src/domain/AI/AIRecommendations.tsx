import React, { useState, useEffect } from 'react';
import { Card, List, Tag, Typography, Space, Button, Alert, Spin } from 'antd';
import { RobotOutlined, ThunderboltOutlined, CheckCircleOutlined } from '@ant-design/icons';
import axios from 'axios';

const { Title, Text, Paragraph } = Typography;

interface ModuleRecommendation {
  moduleName: string;
  provider: string;
  version: string;
  description: string;
  confidenceScore: number;
  reasoning: string;
  category: string;
}

interface OptimizationSuggestion {
  type: string;
  title: string;
  description: string;
  impact: string;
  recommendation: string;
  estimatedSavings: number;
}

interface AIRecommendationsProps {
  organizationId: string;
  workspaceId?: string;
}

const AIRecommendations: React.FC<AIRecommendationsProps> = ({ organizationId, workspaceId }) => {
  const [moduleRecommendations, setModuleRecommendations] = useState<ModuleRecommendation[]>([]);
  const [optimizations, setOptimizations] = useState<OptimizationSuggestion[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const fetchRecommendations = async () => {
    setLoading(true);
    setError(null);
    try {
      const params = workspaceId ? { workspaceId } : {};
      const response = await axios.get(
        `/api/v1/ai/recommendations/${organizationId}`,
        { params }
      );
      setModuleRecommendations(response.data);
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to load AI recommendations');
    } finally {
      setLoading(false);
    }
  };

  const fetchOptimizations = async () => {
    setLoading(true);
    setError(null);
    try {
      const response = await axios.get(`/api/v1/ai/optimizations/${organizationId}`);
      setOptimizations(response.data);
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to load optimization suggestions');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchRecommendations();
    fetchOptimizations();
  }, [organizationId, workspaceId]);

  const getConfidenceColor = (score: number) => {
    if (score >= 0.9) return 'green';
    if (score >= 0.7) return 'blue';
    return 'orange';
  };

  const getImpactColor = (impact: string) => {
    switch (impact.toUpperCase()) {
      case 'HIGH':
        return 'red';
      case 'MEDIUM':
        return 'orange';
      case 'LOW':
        return 'green';
      default:
        return 'default';
    }
  };

  const getTypeColor = (type: string) => {
    switch (type.toUpperCase()) {
      case 'COST':
        return 'gold';
      case 'SECURITY':
        return 'red';
      case 'PERFORMANCE':
        return 'blue';
      case 'COMPLIANCE':
        return 'purple';
      default:
        return 'default';
    }
  };

  if (loading && !moduleRecommendations.length && !optimizations.length) {
    return (
      <div style={{ textAlign: 'center', padding: '40px' }}>
        <Spin size="large" />
        <Paragraph>Loading AI-powered insights...</Paragraph>
      </div>
    );
  }

  return (
    <div>
      <Space direction="vertical" size="large" style={{ width: '100%' }}>
        {error && (
          <Alert
            message="Error"
            description={error}
            type="error"
            showIcon
            closable
            onClose={() => setError(null)}
          />
        )}

        <Card
          title={
            <Space>
              <RobotOutlined />
              <Title level={4} style={{ margin: 0 }}>
                AI Module Recommendations
              </Title>
            </Space>
          }
          extra={
            <Button onClick={fetchRecommendations} loading={loading}>
              Refresh
            </Button>
          }
        >
          <List
            itemLayout="vertical"
            dataSource={moduleRecommendations}
            renderItem={(item) => (
              <List.Item>
                <List.Item.Meta
                  title={
                    <Space>
                      <Text strong>{item.moduleName}</Text>
                      <Tag color={getConfidenceColor(item.confidenceScore)}>
                        {Math.round(item.confidenceScore * 100)}% Confidence
                      </Tag>
                      <Tag>{item.category}</Tag>
                    </Space>
                  }
                  description={
                    <Space direction="vertical" size="small">
                      <Text>Provider: {item.provider} | Version: {item.version}</Text>
                      <Paragraph>{item.description}</Paragraph>
                      <Text type="secondary">
                        <i>Reasoning: {item.reasoning}</i>
                      </Text>
                    </Space>
                  }
                />
              </List.Item>
            )}
          />
        </Card>

        <Card
          title={
            <Space>
              <ThunderboltOutlined />
              <Title level={4} style={{ margin: 0 }}>
                Optimization Suggestions
              </Title>
            </Space>
          }
          extra={
            <Button onClick={fetchOptimizations} loading={loading}>
              Refresh
            </Button>
          }
        >
          <List
            itemLayout="vertical"
            dataSource={optimizations}
            renderItem={(item) => (
              <List.Item>
                <List.Item.Meta
                  avatar={<CheckCircleOutlined style={{ fontSize: '24px', color: '#52c41a' }} />}
                  title={
                    <Space>
                      <Text strong>{item.title}</Text>
                      <Tag color={getTypeColor(item.type)}>{item.type}</Tag>
                      <Tag color={getImpactColor(item.impact)}>{item.impact} Impact</Tag>
                    </Space>
                  }
                  description={
                    <Space direction="vertical" size="small">
                      <Paragraph>{item.description}</Paragraph>
                      <Text>
                        <strong>Recommendation:</strong> {item.recommendation}
                      </Text>
                      {item.estimatedSavings > 0 && (
                        <Text type="success">
                          <strong>Estimated Annual Savings:</strong> ${item.estimatedSavings.toFixed(2)}
                        </Text>
                      )}
                    </Space>
                  }
                />
              </List.Item>
            )}
          />
        </Card>
      </Space>
    </div>
  );
};

export default AIRecommendations;
